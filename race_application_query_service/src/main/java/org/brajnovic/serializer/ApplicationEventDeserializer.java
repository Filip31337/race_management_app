package org.brajnovic.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.brajnovic.event.ApplicationCreatedEvent;
import org.brajnovic.event.ApplicationDeletedEvent;
import org.brajnovic.event.ApplicationEvent;

public class ApplicationEventDeserializer implements Deserializer<ApplicationEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApplicationEvent deserialize(String topic, byte[] data) {
        try {
            JsonNode root = objectMapper.readTree(data);
            String eventType = root.get("eventType").asText();
            if ("APPLICATION_CREATED".equals(eventType)) {
                return objectMapper.treeToValue(root, ApplicationCreatedEvent.class);
            } else if ("APPLICATION_DELETED".equals(eventType)) {
                return objectMapper.treeToValue(root, ApplicationDeletedEvent.class);
            } else {
                throw new IllegalArgumentException("Unknown eventType: " + eventType);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing ApplicationEvent", e);
        }
    }
}
