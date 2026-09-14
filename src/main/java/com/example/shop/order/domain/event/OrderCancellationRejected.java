package com.example.shop.order.domain.event;

import com.example.shop.common.event.publisher.OrderCancellationResult;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class OrderCancellationRejected implements OrderCancellationResult {
    UUID eventId = UUID.randomUUID();

    Instant when;
    Long id;

    String reason;

    public static OrderCancellationResult of(Long id, String reason) {
        return new OrderCancellationRejected(Instant.now(), id, reason);
    }

    @Override
    public String getMessage() {
        return reason;
    }
}
