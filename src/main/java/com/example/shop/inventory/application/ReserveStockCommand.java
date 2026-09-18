package com.example.shop.inventory.application;

public record ReserveStockCommand(String productId, int quantity) { }
