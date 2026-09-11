package com.example.shop.order.domain;

import com.example.shop.order.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private Long id;

    private String customerId;

    private OrderStatus status;

    private List<OrderLine> lines = new ArrayList<>();

    private Instant createdAt;

    public BigDecimal getTotalAmount() {
        return lines.stream()
                .map(OrderLine::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Order cancel() {
        if (OrderStatus.CONFIRMED == status || OrderStatus.FAILED == status) {
            throw new IllegalStateException("Order cannot be cancelled");
        }

        status = OrderStatus.CANCELLED;
        return this;
    }

    public Order save() {
        return this;
    }
}
