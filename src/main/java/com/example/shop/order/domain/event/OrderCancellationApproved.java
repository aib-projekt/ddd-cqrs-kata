package com.example.shop.order.domain.event;

import com.example.shop.common.event.publisher.DomainEvent;
import com.example.shop.order.domain.Order;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class OrderCancellationApproved implements DomainEvent {
    UUID eventId = UUID.randomUUID();

    Instant when;
    Long id;

    public static OrderCancellationApproved of(Order order) {
        return new OrderCancellationApproved(Instant.now(), order.getId());
    }
}
