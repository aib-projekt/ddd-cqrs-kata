package com.example.shop.payment.exception;

import java.math.BigDecimal;

public class PaymentDeclinedException extends RuntimeException {
    public PaymentDeclinedException(Long orderId, BigDecimal amount) {
        super("Payment declined for order %d, amount=%s".formatted(orderId, amount));
    }
}
