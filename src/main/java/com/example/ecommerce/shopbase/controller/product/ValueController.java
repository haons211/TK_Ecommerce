package com.example.ecommerce.shopbase.controller.product;

import com.example.ecommerce.shopbase.dto.request.ValueRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.service.product.ValueCombinationServiceImpl;
import com.example.ecommerce.shopbase.service.product.ValueService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/value")
@Validated
public class ValueController {
    @Autowired
    ValueService valueServiceImpl;
    @Autowired
    private ValueCombinationServiceImpl valueCombinationServiceImpl;

    @PostMapping("")
    public ApiResponse<Value> add(@Valid @RequestBody ValueRequest value) {
            Value savedValue = valueServiceImpl.addValue(value);
            return ApiResponse.<Value>builder()
                    .result(savedValue)
                    .build();

    }
    @GetMapping("")
    public ApiResponse<List<Value>> getAllValues() {
            List<Value> values = valueServiceImpl.getAllValue();
            return ApiResponse.<List<Value>>builder()
                    .result(values)
                    .build();

    }

    @DeleteMapping("/{assortmentId}")
    public ApiResponse<Void> deleteValue(@PathVariable("assortmentId")  int assortmentId) {
            valueServiceImpl.delete(assortmentId);

            return ApiResponse.<Void>builder()
                    .message("Value deleted successfully")
                    .build();

    }



    @PutMapping("/{id}")
    public ApiResponse<Value> updateValue(@PathVariable("id")  int id, @RequestBody @Valid ValueRequest updatedValue) {

            Value updatedEntity = valueServiceImpl.updateValue(id, updatedValue);
                return ApiResponse.<Value>builder()
                        .result(updatedEntity)
                        .build();

    }

}

