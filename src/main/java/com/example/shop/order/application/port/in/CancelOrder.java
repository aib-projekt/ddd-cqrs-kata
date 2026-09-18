package com.example.shop.order.application.port.in;

import com.example.shop.order.application.OrderCancellationCommand;
import com.example.shop.order.domain.event.OrderCancelled;

public interface CancelOrder {
    OrderCancelled cancelOrder(OrderCancellationCommand command);
}
