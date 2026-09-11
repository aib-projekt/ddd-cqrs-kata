package com.example.shop.order.domain.event;

import com.example.shop.order.domain.Order;

public record OrderCancelled (Long id) {
    public static OrderCancelled of(Order order) {
        OrderCancelled orderCancelled = new OrderCancelled(order.getId());
        return orderCancelled;
    }
}
