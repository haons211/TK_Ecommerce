package com.example.ecommerce.shopbase.controller.product;

import com.example.ecommerce.shopbase.dto.request.inventory.QuantityRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.inventory.QuantityResponse;
import com.example.ecommerce.shopbase.service.product.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @PutMapping("/reduce")
    public ApiResponse<QuantityResponse> reduce(@RequestBody @Valid QuantityRequest quantityRequest) {
        inventoryService.reduceProduct(quantityRequest.assortment_value_id(), quantityRequest.quantity());
        return ApiResponse.<QuantityResponse>builder().message("Reduce successfully").build();
    }

    @PutMapping("/increase")
    public ApiResponse<QuantityResponse> increase(@RequestBody @Valid QuantityRequest quantityRequest) {
        inventoryService.increaseProduct(quantityRequest.assortment_value_id(), quantityRequest.quantity());
        return ApiResponse.<QuantityResponse>builder().message("Increase successfully").build();
    }

    @GetMapping("/getPrice/{id}")
    public ApiResponse<Integer> getPrice(@PathVariable("id") int id) {
        int price = inventoryService.getPriceByValue(id);
        return ApiResponse.<Integer>builder()
                .result(price)
                .build();
    }

    @GetMapping("/getMaxPrice")
    public ApiResponse<Integer> getPrice(@RequestBody List<Integer> valueIds) {
        int maxPrice = inventoryService.getMaxPriceByValueIds(valueIds);
        return ApiResponse.<Integer>builder()
                .result(maxPrice)
                .build();
    }

}
