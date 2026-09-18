package com.example.shop.inventory.infrastructure.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ItemEntity {

    @Id
    private String productId;

    private int availableQuantity;
    private int reservedQuantity;

    @ElementCollection
    @CollectionTable(name = "inventory_order_items", joinColumns = @JoinColumn(name = "inventory_id"))
    private List<InventoryOrderLine> orderItems = new ArrayList<>();

    public ItemEntity(String productId, int availableQuantity) {
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = 0;
    }
}
