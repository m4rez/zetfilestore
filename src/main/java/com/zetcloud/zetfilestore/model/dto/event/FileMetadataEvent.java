package com.zetcloud.zetfilestore.model.dto.event;

public record FileMetadataEvent(
        String fileId,
        String fileName,
        String fileType,
        long fileSize,
        String uploadTime,
        String userId
) {}
