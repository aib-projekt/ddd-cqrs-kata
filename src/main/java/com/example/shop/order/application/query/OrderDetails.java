package com.example.shop.order.application.query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

public record OrderDetails(Long id, String status, Instant createdAt, BigDecimal totalAmount, Collection<OrderDetailsLine> orderLines) { }
