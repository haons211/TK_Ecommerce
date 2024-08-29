package com.example.ecommerce.shopbase.controller.product;

import com.example.ecommerce.shopbase.dto.request.ValueCombinationRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import com.example.ecommerce.shopbase.repository.ValueCombinationRepository;
import com.example.ecommerce.shopbase.service.product.ValueCombinationService;
import com.example.ecommerce.shopbase.service.product.ValueCombinationServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product/value-combination")
public class ValueCombinationController {
    @Autowired
    ValueCombinationService valueCombinationService;
    @Autowired
    private ValueCombinationServiceImpl valueCombinationServiceImpl;

    @PostMapping("get-price/")
    public ApiResponse<Integer> getPrice(@RequestBody @Valid ValueCombinationRequest request) {
            int prices = valueCombinationService.getPricesByValueIdsWithBothValues(request);
            return ApiResponse.<Integer>builder()
                    .result(prices)
                    .build();
        }

        @DeleteMapping("/{valueId}")
    public ApiResponse<Void> delete(@PathVariable Integer valueId) {
        valueCombinationServiceImpl.deleteValueCombination(valueId);

        return ApiResponse.<Void>builder()
                .message("Deleted value combination")
                .build();

        }





}


