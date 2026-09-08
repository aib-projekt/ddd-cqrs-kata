package com.example.shop.order.domain;

import com.example.shop.order.aplication.CreateOrderRequest;
import com.example.shop.order.aplication.port.OrderRepository;
import com.example.shop.order.domain.event.OrderCancelled;
import com.example.shop.order.domain.event.OrderCreated;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Order {
    private final OrderRepository orderRepository;

    public OrderCreated createOrder(CreateOrderRequest request) {
        return null;
    }

    public OrderCancelled cancelOrder() {
        return null;
    }

    public OrderCreated getOrder(Long id) {
        return orderRepository.getById(id);
    }
}
