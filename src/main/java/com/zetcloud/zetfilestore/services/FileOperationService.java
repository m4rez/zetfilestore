package com.zetcloud.zetfilestore.services;

import com.zetcloud.zetfilestore.model.dto.event.FileMetadataEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileOperationService {
    ResponseEntity handleFileUpload(String fileId, MultipartFile file, String userId);
    List<FileMetadataEvent> handleFileFetch (String userId);
    byte[] handleFileDownload(String fileName);
}
