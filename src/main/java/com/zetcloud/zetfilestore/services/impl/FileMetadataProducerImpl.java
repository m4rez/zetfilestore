package com.zetcloud.zetfilestore.services.impl;

import com.zetcloud.zetfilestore.model.dto.event.FileMetadataEvent;
import com.zetcloud.zetfilestore.services.FileMetadataProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class FileMetadataProducerImpl implements FileMetadataProducer {

    private final KafkaTemplate<String, FileMetadataEvent> kafkaTemplate;

    @Value("${kafka.topic.fileUpload}")
    private String fileUploadTopic;

    @Autowired
    public FileMetadataProducerImpl(KafkaTemplate<String, FileMetadataEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendFileMetadata(String fileName, String fileType, long fileSize, String userId) {
        FileMetadataEvent event = new FileMetadataEvent(
                UUID.randomUUID().toString(),
                fileName,
                fileType,
                fileSize,
                Instant.now().toString(),
                userId
        );

        CompletableFuture<SendResult<String, FileMetadataEvent>> future = kafkaTemplate.send(fileUploadTopic, event);
        future.whenComplete((result, ex) -> {
            if (ex == null) {

                System.out.println("✅ Kafka send SUCCESS");
                System.out.println("Topic: " + result.getRecordMetadata().topic());
                System.out.println("Partition: " + result.getRecordMetadata().partition());
                System.out.println("Offset: " + result.getRecordMetadata().offset());
            } else {
                System.err.println("❌ Kafka send FAILED: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        System.out.println("Sent FileMetadataEvent to Kafka: " + event); // This logs immediately, before send confirmation
    }
}