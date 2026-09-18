package com.example.shop.inventory.infrastructure.persistence;

import com.example.shop.inventory.domain.InventoryOrderStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryOrderLine {
    private String productId;
    private int quantity;
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private InventoryOrderStatus status;
}
