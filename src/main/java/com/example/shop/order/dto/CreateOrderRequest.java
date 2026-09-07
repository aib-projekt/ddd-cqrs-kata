package com.example.shop.order.dto;

import java.util.List;

public record CreateOrderRequest(String customerId, List<OrderLineRequest> lines) {
}
