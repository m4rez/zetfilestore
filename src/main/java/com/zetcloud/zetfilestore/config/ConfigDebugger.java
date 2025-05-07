package com.zetcloud.zetfilestore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ConfigDebugger {
    @Value("${spring.kafka.consumer.properties.spring.json.trusted.packages:default}")
    private String trustedPackages;

    @Value("${kafka.topic.fileUpload:default}")
    private String topic;

    @PostConstruct
    public void logConfig() {
        System.out.println("zetfilestore - Trusted packages: " + trustedPackages);
        System.out.println("zetfilestore - Topic: " + topic);
    }
}