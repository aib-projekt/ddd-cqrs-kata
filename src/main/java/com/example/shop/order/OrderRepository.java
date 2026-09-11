package com.example.shop.order;

import com.example.shop.order.infrastructure.persistence.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
