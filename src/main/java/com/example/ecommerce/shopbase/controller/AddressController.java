package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.dto.response.AddressResponse;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.service.address.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class AddressController {

    AddressService addressService;

    @GetMapping("/get_child_address_list")
    public ApiResponse<List<AddressResponse>> getChildAddressList(@RequestParam Integer parent_id) {
        List<AddressResponse> addressResponse = addressService.getChildAddressList(parent_id);
        return ApiResponse.<List<AddressResponse>>builder()
                .result(addressResponse)
                .build();
    }
}
