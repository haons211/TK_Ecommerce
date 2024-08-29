package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.dto.inventory.InventoryResult;
import com.example.ecommerce.shopbase.dto.request.CreatePreOrderRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.PreOrderResponse;
import com.example.ecommerce.shopbase.service.order.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

     OrderService orderService;

     @GetMapping("/preOrder/status")
     public ApiResponse<String> checkOrderStatus(@RequestParam String orderId) {
         return ApiResponse.<String>builder()
                 .result(orderService.checkOrderStatus(orderId))
                 .build();
     }

     @GetMapping("/preOrder/{orderId}")
     public ApiResponse<List<CartItemDetails>> getPreOrderDetail(@PathVariable String orderId) throws JsonProcessingException {
         return ApiResponse.<List<CartItemDetails>>builder()
                 .result(orderService.getPreOrderDetail(orderId))
                 .build();
     }

     @PostMapping("/addtoOrder")
     public ApiResponse<PreOrderResponse> addToOrder(@RequestBody CreatePreOrderRequest request) throws JsonProcessingException {
         return ApiResponse.<PreOrderResponse>builder()
                 .result(orderService.createPreOrder(request))
                 .build();
     }

     @KafkaListener(topics = "inventory_result", groupId = "ghtk-Group3")
     public void consumeInventoryResult(InventoryResult message) {

         orderService.updateOrderStatus(message);
     }
}
