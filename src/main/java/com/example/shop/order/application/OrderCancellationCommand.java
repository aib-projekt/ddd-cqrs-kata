package com.example.shop.order.application;

import lombok.Value;

@Value
public class OrderCancellationCommand {
    Long orderId;
}
