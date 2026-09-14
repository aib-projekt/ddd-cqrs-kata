package com.example.shop.inventory.adapter.in;

import com.example.shop.order.domain.event.OrderCancellationApproved;
import org.springframework.context.event.EventListener;

public class InventoryEventHandler {

    @EventListener
    void handleOrderCancellation(OrderCancellationApproved event) {
//        inventoryService.releaseOrderReservation(event.getId());
    }
}
