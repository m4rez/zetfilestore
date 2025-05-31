package com.zetcloud.zetfilestore.services.impl;

import com.zetcloud.zetfilestore.model.dto.event.FileMetadataEvent;
import com.zetcloud.zetfilestore.services.FileMetadataProducer;
import com.zetcloud.zetfilestore.services.FileStorageService;
import com.zetcloud.zetfilestore.services.FileOperationService;

import okhttp3.Response;
import okio.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileOperationServiceImpl implements FileOperationService {

    private final FileStorageService fileStorageService;
    private final FileMetadataProducer fileMetadataProducer;
    @Value("${url.zetmetadata}")
    private String urlZetmetadata;

    @Autowired
    private RestTemplate restTemplate;

    public FileOperationServiceImpl(FileStorageService fileStorageService, FileMetadataProducer fileMetadataProducer) {
        this.fileStorageService = fileStorageService;
        this.fileMetadataProducer = fileMetadataProducer;
    }

    @Override
    public ResponseEntity handleFileUpload(String fileId, MultipartFile file, String userId) {
        // Store file in MinIO
        ResponseEntity response = fileStorageService.storeFile(fileId, file);

        // Send metadata event to Kafka
        fileMetadataProducer.sendFileMetadata(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                userId
        );
        return response;
    }


    public List<FileMetadataEvent> handleFileFetch(String userId) {
        String requestUrl = urlZetmetadata + "/api/metadata" + "?userId=" + userId;
        ResponseEntity<List<FileMetadataEvent>> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FileMetadataEvent>>() {}
        );
        return response.getBody();
    }

    public byte[] handleFileDownload(String fileName){
        return fileStorageService.retrieveFile(fileName);
    }
}


