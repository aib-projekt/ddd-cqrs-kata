package com.example.shop.inventory.application.port.out;

public interface InventoryRepository {
    void cancelItemByOrderId(Long id);
}
