package com.toyin.inventoryservice;

import com.toyin.inventoryservice.model.Inventory;
import com.toyin.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);}
		@Bean
		public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
		Inventory inventory = new Inventory();
		inventory.setSkuCode("iphone_13");
		inventory.setQuantity(100);

		inventoryRepository.save(inventory);

		};
	}

}
