package com.example.shop.order.application;

import com.example.shop.common.event.publisher.EventPublisher;
import com.example.shop.common.event.publisher.OrderCancellationResult;
import com.example.shop.order.application.port.in.CancelOrder;
import com.example.shop.order.application.port.out.OrderRepository;
import com.example.shop.order.domain.Order;
import com.example.shop.order.domain.event.OrderCancellationApproved;
import com.example.shop.order.domain.event.OrderCancellationRejected;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CancelOrderService implements CancelOrder {
    private final EventPublisher eventPublisher;
    private final OrderRepository orderRepository;

    @Override
    public OrderCancellationResult cancelOrder(OrderCancellationCommand command) {
        var orderCancellationResult = orderRepository.getById(command.getOrderId())
                        .map(Order::cancel)
                        .map(orderRepository::save)
                        .map(OrderCancellationApproved::of)
                        .orElse(OrderCancellationRejected.of(command.getOrderId(), "Order not found"));

        eventPublisher.publish(orderCancellationResult);

        return orderCancellationResult;
    }
}
