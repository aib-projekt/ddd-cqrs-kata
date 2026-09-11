package com.example.shop.order.application;

import com.example.shop.order.application.port.in.CancelOrder;
import com.example.shop.order.application.port.out.OrderRepository;
import com.example.shop.order.domain.Order;
import com.example.shop.order.domain.event.OrderCancelled;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CancelOrderService implements CancelOrder {
    private final OrderRepository orderRepository;

    @Override
    public OrderCancelled cancelOrder(Long id) {
        var order = orderRepository.getById(id)
                .map(Order::cancel)
                .map(orderRepository::save)
//                .map(Inventory::cancel)
//                .map(Payment::cancel)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        return OrderCancelled.of(order);
    }
}
