package org.brajnovic.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.brajnovic.event.ApplicationEvent;
import org.brajnovic.event.RaceEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapServers;

    @Bean
    public NewTopic raceTopic() {
        return new NewTopic("race-events", 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, RaceEvent> raceProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, RaceEvent> kafkaTemplate(ProducerFactory<String, RaceEvent> raceProducerFactory) {
        return new KafkaTemplate<>(raceProducerFactory);
    }

    @Bean
    public NewTopic applicationTopic() {
        return new NewTopic("application-events", 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, ApplicationEvent> applicationProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, ApplicationEvent> applicationKafkaTemplate(ProducerFactory<String, ApplicationEvent> applicationProducerFactory) {
        return new KafkaTemplate<>(applicationProducerFactory);
    }
}
