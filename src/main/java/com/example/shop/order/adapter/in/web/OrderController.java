package com.example.shop.order.adapter.in.web;

import com.example.shop.order.application.OrderCancellationCommand;
import com.example.shop.order.application.port.in.CancelOrder;
import com.example.shop.order.application.port.in.RetrieveOrderData;
import com.example.shop.order.application.query.OrderDetails;
import com.example.shop.order.domain.event.OrderCancelled;
import com.example.shop.order.exception.OrderCancellationException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final RetrieveOrderData retrieveOrderData;
    private final CancelOrder cancelOrder;

    @GetMapping("/{id}")
    public OrderDetails getOrder(@PathVariable Long id) {
        return retrieveOrderData.getOrder(id);
    }

    @PostMapping("/{request}/cancel")
    public OrderCancelled cancelOrder(@PathVariable OrderCancellationRequest request) {
        var orderCancellationResult = cancelOrder.cancelOrder(new OrderCancellationCommand(request.orderId));
        if (orderCancellationResult.isCancelled()) {
            return OrderCancelled.of(orderCancellationResult.getId());
        } else {
            throw new OrderCancellationException(orderCancellationResult.getId(), orderCancellationResult.getMessage());
        }
    }

    public record OrderCancellationRequest(Long orderId) { }
}
