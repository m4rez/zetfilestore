package com.zetcloud.zetfilestore.controller;


import com.zetcloud.zetfilestore.model.dto.event.FileMetadataEvent;
import com.zetcloud.zetfilestore.services.FileOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/zetfilestore/file")
public class FileStorageController {

    @Autowired
    FileOperationService fileOperationService;

    @PostMapping("/upload/{fileId}")
    public void uploadFile(
            @PathVariable("fileId") String fileId,
            @RequestParam("file") MultipartFile file) {
        fileOperationService.handleFileUpload(fileId, file, "123");
    }
    @GetMapping("/get")
    public List<FileMetadataEvent> fetchFiles(
            @RequestParam("userId") String userId
    ) {
        return fileOperationService.handleFileFetch(userId);
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName) {
        byte[] fileData = fileOperationService.handleFileDownload(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }
}