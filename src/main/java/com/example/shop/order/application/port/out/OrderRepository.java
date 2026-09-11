package com.example.shop.order.application.port.out;

import com.example.shop.order.application.query.OrderDetails;
import com.example.shop.order.domain.Order;

import java.util.Optional;

public interface OrderRepository {
    Optional<OrderDetails> findById(Long id);

    Optional<Order> getById(Long id);

    Order save(Order order);
}
