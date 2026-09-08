package com.example.shop.order.aplication.port.in;

import com.example.shop.order.aplication.CreateOrderRequest;
import com.example.shop.order.domain.event.OrderCreated;
import org.springframework.http.ResponseEntity;

public interface PlaceOrder {
    ResponseEntity<OrderCreated> createOrder(CreateOrderRequest request);
}
