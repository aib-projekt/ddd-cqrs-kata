package com.example.shop.inventory;

import com.example.shop.inventory.infrastructure.persistence.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<ItemEntity, String> {
}
