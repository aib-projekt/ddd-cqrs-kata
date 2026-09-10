package com.example.shop.order.application.query;

import java.math.BigDecimal;

public record OrderDetailsLine(String productId, int quantity, BigDecimal unitPrice, BigDecimal totalAmount) { }
