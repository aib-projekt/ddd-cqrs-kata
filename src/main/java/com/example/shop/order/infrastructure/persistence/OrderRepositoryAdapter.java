package com.example.shop.order.infrastructure.persistence;

import com.example.shop.order.OrderLineEmbeddable;
import com.example.shop.order.application.port.out.OrderRepository;
import com.example.shop.order.application.query.OrderDetails;
import com.example.shop.order.application.query.OrderDetailsLine;
import com.example.shop.order.domain.Order;
import com.example.shop.order.domain.OrderLine;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderRepositoryAdapter implements OrderRepository {
    private final WrappedOrderRepository wrappedJpaRepo;

    @Override
    public Optional<OrderDetails> findById(Long id) {
        return wrappedJpaRepo.findById(id)
                .map(OrderRepositoryAdapter::mapToOrderDetails);
    }

    @Override
    public Optional<Order> getById(Long id) {
        return wrappedJpaRepo.findById(id)
                .map(OrderRepositoryAdapter::mapToOrder);
    }

    private static Order mapToOrder(OrderEntity orderEntity) {
        var order = new Order();
        order.setId(orderEntity.getId());
        order.setCustomerId(orderEntity.getCustomerId());
        order.setStatus(orderEntity.getStatus());
        order.setCreatedAt(orderEntity.getCreatedAt());
        order.setLines(
                orderEntity.getLines()
                    .stream()
                    .map(OrderRepositoryAdapter::mapToOrderDetailLine)
                    .toList());

        return order;
    }

    private static OrderLine mapToOrderDetailLine(OrderLineEmbeddable orderLine) {
        return new OrderLine(orderLine.getProductId(), orderLine.getQuantity(), orderLine.getUnitPrice());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Order save(Order order) {
        var entity = mapToOrderEntity(order);
        return mapToOrder(wrappedJpaRepo.save(entity));
    }

    @NonNull
    private static OrderEntity mapToOrderEntity(Order order) {
        var orderEntity = new OrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setCustomerId(order.getCustomerId());
        orderEntity.setCreatedAt(order.getCreatedAt());
        orderEntity.setLines(order.getLines().stream()
                .map(OrderRepositoryAdapter::mapToOrderLineEmbeddable)
                .toList());
        return orderEntity;
    }

    private static OrderLineEmbeddable mapToOrderLineEmbeddable(OrderLine orderLine) {
        var line = new OrderLineEmbeddable();
        line.setProductId(orderLine.productId());
        line.setQuantity(orderLine.quantity());
        line.setUnitPrice(orderLine.unitPrice());
        return line;
    }

    @NonNull
    private static OrderDetails mapToOrderDetails(OrderEntity order) {
        var lines = order.getLines().stream()
                .map(OrderRepositoryAdapter::mapToOrderDetailsLine)
                .toList();
        return new OrderDetails(order.getId(), order.getStatus().name(), order.getCreatedAt(), order.getTotalAmount(), lines);
    }

    private static OrderDetailsLine mapToOrderDetailsLine(OrderLineEmbeddable line) {
        return new OrderDetailsLine(line.getProductId(), line.getQuantity(), line.getUnitPrice(), line.lineTotal());
    }

}

interface WrappedOrderRepository extends JpaRepository<OrderEntity, Long> { }