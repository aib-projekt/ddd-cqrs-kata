package com.example.shop.order.aplication.port.in;

import com.example.shop.order.domain.event.OrderCancelled;
import org.springframework.http.ResponseEntity;

public interface CancelOrder {
    ResponseEntity<OrderCancelled> cancelOrder();
}
