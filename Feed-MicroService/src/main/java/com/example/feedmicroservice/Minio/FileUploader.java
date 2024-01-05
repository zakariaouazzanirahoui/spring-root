package com.example.feedmicroservice.Minio;

import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
public class FileUploader {

    private final MinioClient minioClient;

    public FileUploader(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Create a MinIO client.
            String bucketName = "bucket";
            String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            // Get the InputStream from the MultipartFile
            InputStream fileStream = file.getInputStream();

            // Upload the file to the MinIO bucket
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(fileStream, file.getSize(), -1)
                            .build());

            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | MinioException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }



    @GetMapping("getImage/{objectName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String objectName) {
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