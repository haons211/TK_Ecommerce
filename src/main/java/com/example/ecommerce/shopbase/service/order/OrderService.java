package com.example.ecommerce.shopbase.service.order;


import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.dto.inventory.InventoryResult;
import com.example.ecommerce.shopbase.dto.request.CreatePreOrderRequest;
import com.example.ecommerce.shopbase.dto.response.PreOrderResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface OrderService {

    String checkOrderStatus(String orderId);
    PreOrderResponse createPreOrder(CreatePreOrderRequest items) throws JsonProcessingException;

    void updateOrderStatus(InventoryResult result);

    List<CartItemDetails> getPreOrderDetail(String orderId) throws JsonProcessingException;

    int getRealPrice(List<CartItemDetails> items);
}
