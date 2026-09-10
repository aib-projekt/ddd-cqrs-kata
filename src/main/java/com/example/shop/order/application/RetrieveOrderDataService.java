package com.example.shop.order.application;

import com.example.shop.order.application.port.in.RetrieveOrderData;
import com.example.shop.order.application.port.out.OrderRepository;
import com.example.shop.order.application.query.OrderDetails;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RetrieveOrderDataService implements RetrieveOrderData {
    private final OrderRepository orderRepository;

    @Override
    public OrderDetails getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}
