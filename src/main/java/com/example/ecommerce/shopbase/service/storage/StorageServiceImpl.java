package com.example.ecommerce.shopbase.service.storage;


import com.example.ecommerce.shopbase.dto.response.SignedURLResponse;
import com.example.ecommerce.shopbase.entity.User;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import com.google.cloud.storage.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StorageServiceImpl implements StorageService {

    @NonFinal
    @Value("${storage.bucketName}")
    String BUCKET_NAME;

    @NonFinal
    @Value("${cloud.cdn.host}")
    String CDN_HOST;

    Storage storage;

    SecurityUtils securityUtils;

    @Override
    public SignedURLResponse getSignedUrl() {
        User user = securityUtils.getCurrentUserLogin();
        String fileName = String.format("%s/%s", user.getId(), UUID.randomUUID());

        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(BUCKET_NAME, fileName)).build();

        Map<String, String> extensionHeaders = new HashMap<>();

        URL url = storage.signUrl(
                blobInfo,
                15,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                Storage.SignUrlOption.withV4Signature());

        return SignedURLResponse.builder()
                .fileName(fileName)
                .url(url.toString())
                .build();
    }

    @Override
    public String getObjectURL(String objectName) {
        if(objectName.contains("salt.tikicdn.com")) {
            return objectName;
        }
        StringBuilder sb = new StringBuilder();
        return sb.append(CDN_HOST).append('/').append(objectName).toString();
    }
}
