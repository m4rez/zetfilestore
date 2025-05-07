package com.zetcloud.zetfilestore.services;

public interface FileMetadataProducer {
    void sendFileMetadata(String fileName, String fileType, long fileSize, String userId);
}
