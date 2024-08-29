package com.example.ecommerce.shopbase.controller;


import com.example.ecommerce.shopbase.dto.OrderSuccessRequest;
import com.example.ecommerce.shopbase.dto.response.AddressResponse;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.inventory.SellerOrderItemResponse;
import com.example.ecommerce.shopbase.dto.response.inventory.ViewOrderItemResponse;
import com.example.ecommerce.shopbase.entity.Order;
import com.example.ecommerce.shopbase.entity.OrderItem;
import com.example.ecommerce.shopbase.mapper.OrderItemMapper;
import com.example.ecommerce.shopbase.repository.OrderItemRepository;
import com.example.ecommerce.shopbase.repository.OrderRepository;
import com.example.ecommerce.shopbase.service.OrderItem.OrderItemService;
import com.example.ecommerce.shopbase.service.payment.IpnResponse;
import com.google.protobuf.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-item")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemController {
  OrderItemService orderItemService;
  OrderRepository orderRepository;
  OrderItemRepository orderItemRepository;
  OrderItemMapper orderItemMapper;


//  @GetMapping("/{cartItemId}")
//  public ApiResponse<Void> update(@PathVariable("cartItemId") int  cartItemId) {
//    orderItemService.updateCartItemInOrderItem(cartItemId);
//    return ApiResponse.<Void>builder()
//        .message("Updated orderItem")
//        .build();
//
//  }
  @PostMapping("/success")
  public ApiResponse<List<ViewOrderItemResponse>> success(@RequestBody OrderSuccessRequest request) {
    Order order = orderRepository.findByOrderCode(request.getOrder_code());
    List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getID());
    List<ViewOrderItemResponse> responseList = orderItems.stream()
            .map(orderItemMapper::toViewOrderItemResponse) // Use method reference
            .collect(Collectors.toList());
    // Return the list wrapped in an ApiResponse
    return ApiResponse.<List<ViewOrderItemResponse>>builder()
            .result(responseList) // Kết quả là URL thanh toán
            .build();

  }




}
