package com.example.ecommerce.shopbase.config.storage;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class StorageConfig {

    @Value("${storage.credential.file.path}")
    private String credentialsPath;

    @Value("${storage.project.id}")
    private String projectId;

    @Bean
    public Storage storage() throws IOException {
        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(credentialsPath)))
                .setProjectId(projectId)
                .build()
                .getService();
    }
}
