package com.example.shop.inventory;

import com.example.shop.inventory.exception.InsufficientStockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

// SMELL: business logic (availability checks, reservation, release) lives in
// the "service" layer right alongside repository calls. InventoryItem is just
// a bag of fields - none of these operations is a method on the entity.
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void reserveStock(String productId, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Unknown product: " + productId));

        if (item.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException(productId, quantity, item.getAvailableQuantity());
        }

        item.setAvailableQuantity(item.getAvailableQuantity() - quantity);
        item.setReservedQuantity(item.getReservedQuantity() + quantity);
        inventoryRepository.save(item);
    }

    @Transactional
    public void releaseStock(String productId, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Unknown product: " + productId));

        int released = Math.min(quantity, item.getReservedQuantity());
        item.setReservedQuantity(item.getReservedQuantity() - released);
        item.setAvailableQuantity(item.getAvailableQuantity() + released);
        inventoryRepository.save(item);
    }

    @Transactional
    public void confirmStock(String productId, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Unknown product: " + productId));

        int confirmed = Math.min(quantity, item.getReservedQuantity());
        item.setReservedQuantity(item.getReservedQuantity() - confirmed);
        inventoryRepository.save(item);
    }

    @Transactional
    public void restock(String productId, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId)
                .orElseGet(() -> new InventoryItem(productId, 0));
        item.setAvailableQuantity(item.getAvailableQuantity() + quantity);
        inventoryRepository.save(item);
    }

    public InventoryItem getAvailability(String productId) {
        return inventoryRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Unknown product: " + productId));
    }
}
