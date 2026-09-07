package com.example.shop.order.dto;

import java.math.BigDecimal;

public record OrderLineRequest(String productId, int quantity, BigDecimal unitPrice) {
}
