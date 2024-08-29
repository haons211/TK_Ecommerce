package com.example.ecommerce.shopbase.service.user;

import com.example.ecommerce.shopbase.dto.request.RegisterSellerRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateSellerRequest;
import com.example.ecommerce.shopbase.dto.response.SellerProfileResponse;
import com.example.ecommerce.shopbase.dto.response.SellerResponse;
import com.example.ecommerce.shopbase.dto.response.inventory.SellerOrderItemResponse;
import com.example.ecommerce.shopbase.entity.OrderItem;

import java.util.List;

public interface SellerService {

    void register(RegisterSellerRequest request);

    SellerResponse getInfo(Integer sellerId);

    SellerResponse updateSeller(Integer sellerId, UpdateSellerRequest request);

    Integer getWardId(Integer sellerId);

    SellerProfileResponse getSellerProfile();

    List<SellerOrderItemResponse> getOrderItemResponse(String status);
}
