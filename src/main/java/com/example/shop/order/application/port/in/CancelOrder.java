package com.example.shop.order.application.port.in;

import com.example.shop.common.event.publisher.OrderCancellationResult;
import com.example.shop.order.application.OrderCancellationCommand;

public interface CancelOrder {
    OrderCancellationResult cancelOrder(OrderCancellationCommand command);
}
