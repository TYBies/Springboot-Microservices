package com.toyin.inventoryservice.service;

import com.toyin.inventoryservice.dto.InventoryResponse;
import com.toyin.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInstock(List<String> skuCode){
       return inventoryRepository.findAllBySkuCodeIn(skuCode)
               .stream()
               .map(inventory ->
                   InventoryResponse.builder()
                           .skuCode(inventory.getSkuCode())
                           .isInStock(inventory.getQuantity()>0)
                           .build()
       ).toList();
    }
}
