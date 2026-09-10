package com.example.shop.order.application.port.out;

import com.example.shop.order.application.query.OrderDetails;

import java.util.Optional;

public interface OrderRepository {
    Optional<OrderDetails> findById(Long id);
}
