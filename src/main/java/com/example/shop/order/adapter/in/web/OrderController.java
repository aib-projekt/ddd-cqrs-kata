package com.example.shop.order.adapter.in.web;

import com.example.shop.order.aplication.CreateOrderRequest;
import com.example.shop.order.aplication.port.in.CancelOrder;
import com.example.shop.order.aplication.port.in.PlaceOrder;
import com.example.shop.order.aplication.port.in.RetrieveOrderData;
import com.example.shop.order.domain.Order;
import com.example.shop.order.domain.event.OrderCancelled;
import com.example.shop.order.domain.event.OrderCreated;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController implements CancelOrder, PlaceOrder, RetrieveOrderData {

    private final Order order;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderCreated> createOrder(CreateOrderRequest request) {
        return ResponseEntity.internalServerError().build();
    }

    @Override
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderCancelled> cancelOrder() {
        return ResponseEntity.internalServerError().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<OrderCreated> getOrder(String id) {
        return ResponseEntity.of(Optional.of(order.getOrder(id)));
    }
}
