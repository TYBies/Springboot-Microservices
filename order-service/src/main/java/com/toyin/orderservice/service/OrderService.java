package com.toyin.orderservice.service;

import com.toyin.orderservice.dto.InventoryResponse;
import com.toyin.orderservice.dto.OrderLineItemsDTO;
import com.toyin.orderservice.dto.OrderRequest;
import com.toyin.orderservice.model.Order;
import com.toyin.orderservice.model.OrderLineItems;
import com.toyin.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequest){
        //
        List<OrderLineItems>  orderLineItems = orderRequest.getOrderLineItemsDTOList().stream()
                .map(this::mapToDto)
                .toList();
        //Prepare Order for Web request


        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //Call Inventory Service, and place Order if Product is in stock

       InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();

       boolean allProductInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

       if(allProductInStock){
           orderRepository.save(order);
       }else {
           throw new IllegalArgumentException("Product is not in Stock, please try again later");
       }
    }

    //private void prepareOrderForWeb(List<OrderLineItems> orderLineItems) {
    //}

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
