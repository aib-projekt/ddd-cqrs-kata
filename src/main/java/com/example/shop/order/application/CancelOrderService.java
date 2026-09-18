package com.example.shop.order.application;

import com.example.shop.common.event.publisher.EventPublisher;
import com.example.shop.order.application.port.in.CancelOrder;
import com.example.shop.order.application.port.out.OrderRepository;
import com.example.shop.order.common.exception.OrderNotFoundException;
import com.example.shop.order.domain.Order;
import com.example.shop.order.domain.event.OrderCancellationApproved;
import com.example.shop.order.domain.event.OrderCancelled;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CancelOrderService implements CancelOrder {
    private final EventPublisher eventPublisher;
    private final OrderRepository orderRepository;

    @Override
    public OrderCancelled cancelOrder(OrderCancellationCommand command) {
        var order = orderRepository.getById(command.getOrderId())
                .map(Order::cancel)
                .map(orderRepository::save)
                .orElseThrow(() -> new OrderNotFoundException(command.getOrderId()));

        eventPublisher.publish(OrderCancellationApproved.of(order));

        return OrderCancelled.of(order);
    }
}
