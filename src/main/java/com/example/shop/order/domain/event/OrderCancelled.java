package com.example.shop.order.domain.event;

import com.example.shop.order.domain.Order;

public record OrderCancelled (Long id) {
    public static OrderCancelled of(Order orderId) {
        return new OrderCancelled(orderId.getId());
    }
}
