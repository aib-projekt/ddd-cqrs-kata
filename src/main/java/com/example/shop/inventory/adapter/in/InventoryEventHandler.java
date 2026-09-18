package com.example.shop.inventory.adapter.in;

import com.example.shop.inventory.application.InventoryService;
import com.example.shop.inventory.application.ReleaseStockCommand;
import com.example.shop.order.domain.event.OrderCancellationApproved;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
public class InventoryEventHandler {
    private final InventoryService inventoryService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handleOrderCancellation(OrderCancellationApproved event) {
        log.info("Order cancellation event received: {}", event);
        inventoryService.releaseStockForOrder(new ReleaseStockCommand(event.getId()));
    }
}
