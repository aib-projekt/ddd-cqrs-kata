package com.example.shop.order.domain.event;

import com.example.shop.common.event.publisher.OrderCancellationResult;
import com.example.shop.order.domain.Order;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class OrderCancellationApproved implements OrderCancellationResult {
    UUID eventId = UUID.randomUUID();

    Instant when;
    Long id;

    public static OrderCancellationResult of(Order order) {
        return new OrderCancellationApproved(Instant.now(), order.getId());
    }

    @Override
    public boolean isCancelled() {
        return true;
    }
}
