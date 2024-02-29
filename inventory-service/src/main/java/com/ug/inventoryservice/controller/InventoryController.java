package com.ug.inventoryservice.controller;

import com.ug.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class InventoryController {

    @Autowired private InventoryService inventoryService;

    @PostMapping("/publish")
    public void reduceQuantity(
            @RequestParam("id") UUID txnId,
            @RequestParam("itemId") Long itemId,
            @RequestParam("quantity") int quantity
    ){
        inventoryService.reduceQuantity(txnId, itemId, quantity);
    }

}
