package com.example.ecommerce.shopbase.controller.product;

import com.example.ecommerce.shopbase.dto.request.ProductAttributeRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.ProductAttributeResponse;
import com.example.ecommerce.shopbase.dto.response.ValueCombinationResponse;
import com.example.ecommerce.shopbase.entity.ProductAttribute;
import com.example.ecommerce.shopbase.service.product.ProductAttributeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/v1/productAtribute")
public class ProductAtributeController {
    @Autowired
    ProductAttributeService productAttributeServiceImpl;

    @PostMapping
    public ApiResponse<ProductAttributeResponse> add(@RequestBody @Valid ProductAttributeRequest productAttribute) {
        ProductAttribute savedAttribute = productAttributeServiceImpl.add(productAttribute);

        ProductAttributeResponse response = ProductAttributeResponse.builder()
                .productId(savedAttribute.getProduct().getId())
                .name(savedAttribute.getName())
                .value(savedAttribute.getValue())
                .build();

        return ApiResponse.<ProductAttributeResponse>builder()
                .result(response)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductAttributeResponse> update(@PathVariable("id") int id, @RequestBody @Valid ProductAttributeRequest updatedProductAttribute) {
        ProductAttribute updatedEntity = productAttributeServiceImpl.update(id, updatedProductAttribute);

        ProductAttributeResponse response = ProductAttributeResponse.builder()
                .productId(updatedEntity.getProduct().getId())
                .name(updatedEntity.getName())
                .value(updatedEntity.getValue())
                .build();

        return ApiResponse.<ProductAttributeResponse>builder()
                .result(response)
                .build();
    }
    @GetMapping
    public ApiResponse<List<ProductAttributeResponse>> getAllValues() {
        List<ProductAttribute> allAttributes = productAttributeServiceImpl.getAllProductAttributes();

        List<ProductAttributeResponse> responseList = new ArrayList<>();
        for (ProductAttribute productAttribute : allAttributes) {
            ProductAttributeResponse response = ProductAttributeResponse.builder()
                    .productId(productAttribute.getProduct().getId())
                    .name(productAttribute.getName())
                    .value(productAttribute.getValue())
                    .build();
            responseList.add(response);
        }

        return ApiResponse.<List<ProductAttributeResponse>>builder()
                .result(responseList)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteValue(@PathVariable("id") int id) {
        productAttributeServiceImpl.delete(id);
        return ApiResponse.<Void>builder()
                .message("Product attribute deleted")
                .build();
    }




}
