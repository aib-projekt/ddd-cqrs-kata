package com.example.shop.inventory.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productId, int requested, int available) {
        super("Insufficient stock for %s: requested=%d, available=%d"
                .formatted(productId, requested, available));
    }
}
