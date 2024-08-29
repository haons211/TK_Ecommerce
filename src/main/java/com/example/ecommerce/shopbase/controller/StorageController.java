package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.dto.response.SignedURLResponse;
import com.example.ecommerce.shopbase.service.storage.StorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class StorageController {

    StorageService storageService;

    @GetMapping("/uploads/signedUrl")
    public ApiResponse<SignedURLResponse> generateSignedURl() throws Exception {
        return ApiResponse.<SignedURLResponse>builder()
                .result(storageService.getSignedUrl())
                .build();
    }
}
