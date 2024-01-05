package com.example.feedmicroservice.Services;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
@Service
public class MinioService {

    private final MinioClient minioClient;


    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadImage(MultipartFile file) {
        try {
            String bucketName = "bucket";
            String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            InputStream fileStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(fileStream, file.getSize(), -1)
                            .build());
            System.out.println("Post created successfully. Image URL: " + objectName);

            return objectName;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | MinioException e) {
            e.printStackTrace();
            return "Error uploading file";
        }
    }




    public ResponseEntity<byte[]> getImage( String objectName) {
        try {
            InputStream imageStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket("bucket")
                            .object(objectName)
                            .build()
            );

            byte[] imageBytes = imageStream.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (MinioException | IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
