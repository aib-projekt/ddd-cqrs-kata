package com.example.shop.order.application;

import com.example.shop.order.application.port.in.CancelOrder;
import com.example.shop.order.domain.event.OrderCancelled;

public class CancelOrderService implements CancelOrder {

    @Override
    public OrderCancelled cancelOrder(Long id) {
        return null;
    }
}
