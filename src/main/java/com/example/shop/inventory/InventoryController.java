package com.example.shop.inventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// SMELL: the controller returns the JPA entity (InventoryItem) directly as
// JSON - the write/read model and the API model are the same object.
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public InventoryItem getAvailability(@PathVariable String productId) {
        return inventoryService.getAvailability(productId);
    }

    @PostMapping("/{productId}/restock")
    public InventoryItem restock(@PathVariable String productId, @RequestParam int quantity) {
        inventoryService.restock(productId, quantity);
        return inventoryService.getAvailability(productId);
    }
}
