package com.ug.inventoryservice.repository;

import com.ug.inventoryservice.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{

    @Transactional
    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = i.quantity - :quantity " +
            "WHERE i.quantity >= :quantity AND " +
            "i.id = :id")
    int reduceQuantity(Long id,int quantity);

    Inventory save(Inventory inventory);
}
