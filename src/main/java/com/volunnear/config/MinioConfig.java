package com.volunnear.config;

import io.minio.MinioClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${MINIO_UPLOAD_URL}")
    private String minioUrl;

    @Value("${MINIO_ACCESS_KEY}")
    private String minioAccessKey;

    @Value("${MINIO_SECRET_KEY}")
    private String minioSecretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(minioAccessKey, minioSecretKey)
            .build();
    }
}
