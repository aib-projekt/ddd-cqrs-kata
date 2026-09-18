package com.example.shop.inventory.application;

import com.example.shop.inventory.application.port.in.ReleaseStock;
import com.example.shop.inventory.application.port.in.ReserveStock;
import com.example.shop.inventory.application.port.out.InventoryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryService implements ReserveStock, ReleaseStock {
    private final InventoryRepository inventoryRepository;

    @Override
    public void reserveStock(ReserveStockCommand command) {

    }

    @Override
    public void releaseStockForOrder(ReleaseStockCommand command) {
        inventoryRepository.cancelItemByOrderId(command.orderId());
    }
}
