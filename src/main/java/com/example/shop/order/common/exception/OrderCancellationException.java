package com.example.shop.order.common.exception;

public class OrderCancellationException extends RuntimeException {
    public OrderCancellationException(Long id, String message) {
        super("Order cannot be cancelled: " + id + ". Reason: " + message);
    }
}
