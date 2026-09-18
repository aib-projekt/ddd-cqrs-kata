package com.example.shop.inventory.application.port.in;

import com.example.shop.inventory.application.ReleaseStockCommand;

public interface ReleaseStock {
    void releaseStockForOrder(ReleaseStockCommand command);
}
