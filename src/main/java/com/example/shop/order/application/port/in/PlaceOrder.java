package com.example.shop.order.application.port.in;

import com.example.shop.order.application.CreateOrderRequest;
import com.example.shop.order.domain.event.OrderCreated;

public interface PlaceOrder {
    OrderCreated createOrder(CreateOrderRequest request);
}
