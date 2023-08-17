package com.toyin.inventoryservice.repository;

import com.toyin.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    List<Inventory> findAllBySkuCodeIn(List<String> skuCode);

}
