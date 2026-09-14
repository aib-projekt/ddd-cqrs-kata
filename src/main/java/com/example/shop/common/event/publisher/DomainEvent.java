package com.example.shop.common.event.publisher;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

    UUID getEventId();
    Instant getWhen();

    Long getId();
}
