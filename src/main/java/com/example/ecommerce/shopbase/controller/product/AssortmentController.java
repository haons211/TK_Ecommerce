package com.example.ecommerce.shopbase.controller.product;

import com.example.ecommerce.shopbase.dto.product.AssortmentValueListDTO;
import com.example.ecommerce.shopbase.dto.request.AssortmentRequest;
import com.example.ecommerce.shopbase.dto.request.AssortmentValueCombinationRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.entity.Assortment;
import com.example.ecommerce.shopbase.service.product.AssortmentService;
import com.example.ecommerce.shopbase.service.product.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assortment")

public class AssortmentController {
    @Autowired
    AssortmentService assortmentServiceImpl;



    @GetMapping("/{productId}")
    public ApiResponse<List<AssortmentValueCombinationRequest>> getAssortmentvalueCombination(@PathVariable("productId") int productId){

        return ApiResponse.<List<AssortmentValueCombinationRequest>>builder()
                .result(assortmentServiceImpl.getAssortmentValueCombination(productId))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") int id) {
        assortmentServiceImpl.delete(id);
        return ApiResponse.<Void>builder()
                .message("Deleted assortment")
                .build();
    }


    @PutMapping()
    public ApiResponse<AssortmentValueCombinationRequest> updateAssortment(@RequestBody AssortmentValueCombinationRequest assortmentValueCombinationRequest) {
        return ApiResponse.<AssortmentValueCombinationRequest>builder()
                .result(assortmentServiceImpl.updateAssortmentValueCombination( assortmentValueCombinationRequest))
                .build();

    }


    @PostMapping("/add-assortment-value")
    public ApiResponse<AssortmentValueCombinationRequest> addAssortmentValue(@RequestBody AssortmentValueCombinationRequest assortmentValueCombinationRequest) {
        return ApiResponse.<AssortmentValueCombinationRequest>builder()
                .result(assortmentServiceImpl.addAssortmentValueCombination(assortmentValueCombinationRequest))
                .build();

    }


}


