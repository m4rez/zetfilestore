package com.zetcloud.zetfilestore.services.impl;

import com.zetcloud.zetfilestore.services.FileStorageService;
import io.minio.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    public FileStorageServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    private final MinioClient minioClient;

    public ResponseEntity storeFile(String fileId, MultipartFile file) {

        try {
            String bucketName = "file-uploads";
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return ResponseEntity.ok(Map.of("message", "File stored successfully!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "File storage failed: " + e.getMessage()));
        }
    }

    @Override
    public byte[] retrieveFile(String objectName) {
        String bucketName = "file-uploads";
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            byte[] fileBytes = stream.readAllBytes();
            stream.close();
            return fileBytes;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve file from MinIO: " + e.getMessage());
        }
    }

}
