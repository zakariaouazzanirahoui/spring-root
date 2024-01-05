package com.example.feedmicroservice.Config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Value("${minio.accessKey}")
    private String minioAccessKey;

    @Value("${minio.secretKey}")
    private String minioSecretKey;

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("bucket").build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("bucket").build());
        }
        return minioClient;
    }



}
