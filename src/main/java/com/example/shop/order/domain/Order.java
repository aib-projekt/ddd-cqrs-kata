package com.example.shop.order.domain;

import com.example.shop.order.OrderStatus;
import com.example.shop.order.common.exception.OrderCancellationException;
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
            throw new OrderCancellationException(id, "Incorrect status: " + status);
        }

        status = OrderStatus.CANCELLED;
        return this;
    }
}
