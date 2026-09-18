package com.example.shop.inventory.domain;

public record OrderItems(String productId, int quantity, Long orderId) { }
