package com.ug.inventoryservice.service;

import java.util.UUID;

public interface InventoryService {

    public void reduceQuantity(UUID txnId, Long itemId, int quantity);

}
