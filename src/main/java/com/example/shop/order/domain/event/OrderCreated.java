package com.example.shop.order.domain.event;

public record OrderCreated (String id, String status, String createdAt, String totalAmount) { }
