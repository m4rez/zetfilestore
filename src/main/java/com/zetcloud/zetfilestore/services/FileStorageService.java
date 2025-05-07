package com.zetcloud.zetfilestore.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    ResponseEntity storeFile(String fileId, MultipartFile file);
    byte[] retrieveFile(String fileName);
}
