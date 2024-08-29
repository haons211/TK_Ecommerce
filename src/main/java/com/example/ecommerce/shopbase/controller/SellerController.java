package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.dto.request.RegisterSellerRequest;
import com.example.ecommerce.shopbase.dto.request.UpdateSellerRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.SellerProfileResponse;
import com.example.ecommerce.shopbase.dto.response.SellerResponse;
import com.example.ecommerce.shopbase.dto.response.inventory.SellerOrderItemResponse;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.OrderItem;
import com.example.ecommerce.shopbase.service.product.ProductService;
import com.example.ecommerce.shopbase.service.user.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class SellerController {

    ProductService productServiceImpl;
    SellerService sellerService;

    @PostMapping("/register")
    public ApiResponse<Void> registerSeller(@RequestBody RegisterSellerRequest request) {
        sellerService.register(request);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }

    @GetMapping("/profile")
    public ApiResponse<SellerProfileResponse> getSellerProfile() {
        return ApiResponse.<SellerProfileResponse>builder()
                .result(sellerService.getSellerProfile())
                .build();
    }

    @GetMapping("/{sellerId}")
    public ApiResponse<SellerResponse> getSellerInfo(@PathVariable Integer sellerId) {
        return ApiResponse.<SellerResponse>builder()
                .result(sellerService.getInfo(sellerId))
                .build();
    }

    @PutMapping("/{sellerId}")
    public ApiResponse<SellerResponse> updateSeller(
            @PathVariable Integer sellerId,
            @RequestBody UpdateSellerRequest request) {
        return ApiResponse.<SellerResponse>builder()
                .result(sellerService.updateSeller(sellerId, request))
                .build();
    }


    @GetMapping("getWardIdByProduct/{id}")
    public ApiResponse<Integer> getWardId(@PathVariable("id") Integer id) {
        int sellerId = productServiceImpl.getSellerId(id);
        return ApiResponse.<Integer>builder()
                .result(sellerService.getWardId(sellerId))
                .build();
    }
    @GetMapping("/order1")
    public ApiResponse<List<SellerOrderItemResponse>> getOrderItemResponse(@RequestParam("status") String status){
        return ApiResponse.<List<SellerOrderItemResponse>>builder()
                .result(sellerService.getOrderItemResponse(status))
                .build();
    }



}
