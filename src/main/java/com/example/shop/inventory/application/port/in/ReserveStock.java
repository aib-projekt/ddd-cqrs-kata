package com.example.shop.inventory.application.port.in;

import com.example.shop.inventory.application.ReserveStockCommand;

public interface ReserveStock {
    void reserveStock(ReserveStockCommand command);
}
