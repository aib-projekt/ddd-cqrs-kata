package com.example.shop.order;

import com.example.shop.inventory.InventoryService;
import com.example.shop.inventory.exception.InsufficientStockException;
import com.example.shop.order.dto.CreateOrderRequest;
import com.example.shop.order.dto.OrderLineRequest;
import com.example.shop.order.exception.OrderNotFoundException;
import com.example.shop.payment.PaymentService;
import com.example.shop.payment.exception.PaymentDeclinedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// SMELL (the main thing to refactor here):
// 1) OrderService knows InventoryService and PaymentService directly - three
//    "bounded contexts" are coupled through synchronous calls, not through
//    ports or domain events.
// 2) Reads and writes go through the same model (Order = JPA entity) - no
//    CQRS.
// 3) Compensation (releasing already-reserved stock after a failed payment,
//    or after a partial reservation) is hand-rolled in loops in this
//    method - in practice a very primitive, fragile version of a
//    choreographed saga baked straight into the application service code.
// 4) cancelOrder does NOT release stock or refund the payment at all -
//    left deliberately as something to notice and fix during the
//    refactoring.
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository,
                         InventoryService inventoryService,
                         PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        if (request.lines() == null || request.lines().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one line");
        }

        Order order = new Order();
        order.setCustomerId(request.customerId());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());
        order.setLines(toOrderLines(request.lines()));
        order = orderRepository.save(order);

        List<OrderLineRequest> reserved = new ArrayList<>();
        try {
            for (OrderLineRequest line : request.lines()) {
                inventoryService.reserveStock(line.productId(), line.quantity());
                reserved.add(line);
            }
        } catch (InsufficientStockException e) {
            // manual compensation for whatever already got reserved
            for (OrderLineRequest line : reserved) {
                inventoryService.releaseStock(line.productId(), line.quantity());
            }
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            throw e;
        }

        order.setStatus(OrderStatus.STOCK_RESERVED);
        orderRepository.save(order);

        try {
            paymentService.charge(order.getId(), order.getTotalAmount());
        } catch (PaymentDeclinedException e) {
            for (OrderLineRequest line : request.lines()) {
                inventoryService.releaseStock(line.productId(), line.quantity());
            }
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            throw e;
        }

        for (OrderLineRequest line : request.lines()) {
            inventoryService.confirmStock(line.productId(), line.quantity());
        }
        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getOrder(orderId);
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel a confirmed order: " + orderId);
        }
        // TODO / deliberate gap: doesn't release stock or refund the payment.
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private List<OrderLine> toOrderLines(List<OrderLineRequest> lines) {
        List<OrderLine> result = new ArrayList<>();
        for (OrderLineRequest line : lines) {
            result.add(new OrderLine(line.productId(), line.quantity(), line.unitPrice()));
        }
        return result;
    }
}
