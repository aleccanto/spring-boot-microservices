package br.com.microservicos.inventoryservice.controller;

import br.com.microservicos.inventoryservice.model.Inventory;
import br.com.microservicos.inventoryservice.repository.InventoryRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository repository;

    @GetMapping("/{skuCode}")
    public Boolean isInStock(@PathVariable String skuCode) {
        Inventory inventory = repository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("Cannot find product by sku code " + skuCode));
        return inventory.getStock() > 0;
    }
}
