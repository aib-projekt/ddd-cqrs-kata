package com.example.shop.common.event.publisher;

public interface OrderCancellationResult extends DomainEvent {
    default boolean isCancelled() { return false; }
    default String getMessage() { return ""; }
}
