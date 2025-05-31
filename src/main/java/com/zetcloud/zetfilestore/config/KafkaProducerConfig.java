package com.zetcloud.zetfilestore.config;

import com.zetcloud.zetfilestore.model.dto.event.FileMetadataEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;
    private final SslBundles sslBundles;

    public KafkaProducerConfig(KafkaProperties kafkaProperties, SslBundles sslBundles) {
        this.kafkaProperties = kafkaProperties;
        this.sslBundles = sslBundles;
    }

    @Bean
    public ProducerFactory<String, FileMetadataEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.getProducer().buildProperties(sslBundles));

        List<String> bootstrapServers = kafkaProperties.getBootstrapServers();
        if (bootstrapServers != null && !bootstrapServers.isEmpty()) {
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", bootstrapServers));
        }

        JsonSerializer<FileMetadataEvent> valueSerializer = new JsonSerializer<>();
        valueSerializer.setAddTypeInfo(false);

        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), valueSerializer);
    }

    @Bean
    public KafkaTemplate<String, FileMetadataEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}