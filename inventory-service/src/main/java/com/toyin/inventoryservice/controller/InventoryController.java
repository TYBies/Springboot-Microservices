package com.toyin.inventoryservice.controller;

import com.toyin.inventoryservice.dto.InventoryResponse;
import com.toyin.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
/**
 * This Method requires List of SkuCodes of an Order sent through  Http request
 * the SkuCodes are then checked for availability in Stock
 * */
    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String > skuCode){
        return inventoryService.isInstock(skuCode);
    }
 }
