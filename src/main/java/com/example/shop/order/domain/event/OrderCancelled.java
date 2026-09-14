package com.example.shop.order.domain.event;

public record OrderCancelled (Long id) {
    public static OrderCancelled of(Long orderId) {
        return new OrderCancelled(orderId);
    }
}
