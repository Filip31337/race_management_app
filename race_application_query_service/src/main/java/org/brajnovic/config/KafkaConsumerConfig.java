package org.brajnovic.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.brajnovic.event.ApplicationEvent;
import org.brajnovic.event.RaceEvent;
import org.brajnovic.serializer.ApplicationEventDeserializer;
import org.brajnovic.serializer.RaceEventDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, RaceEvent> raceConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "query-service-race");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RaceEventDeserializer.class);
        props.put("spring.json.trusted.packages", "org.brajnovic.event");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RaceEvent> raceKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RaceEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(raceConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ApplicationEvent> applicationConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "query-service-application");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ApplicationEventDeserializer.class);
        props.put("spring.json.trusted.packages", "org.brajnovic.event");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApplicationEvent> applicationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ApplicationEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(applicationConsumerFactory());
        return factory;
    }
}
