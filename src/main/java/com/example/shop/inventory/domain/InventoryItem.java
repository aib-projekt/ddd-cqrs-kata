package com.example.shop.inventory.domain;

import lombok.Value;

import java.util.Collection;

@Value
public class InventoryItem {
    String productId;

    int availableQuantity;
    int reservedQuantity;

    Collection<OrderItems> orderItems;
}
