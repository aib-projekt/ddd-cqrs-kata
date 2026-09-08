package com.example.shop.order.aplication.port.in;

import com.example.shop.order.domain.event.OrderCreated;
import org.springframework.http.ResponseEntity;

public interface RetrieveOrderData {
    ResponseEntity<OrderCreated> getOrder(String orderId);
}
