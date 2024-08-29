package com.example.ecommerce.shopbase.service.storage;

import com.example.ecommerce.shopbase.dto.response.SignedURLResponse;


public interface StorageService {

    SignedURLResponse getSignedUrl();

    String getObjectURL(String objectName);
}
