package org.brajnovic.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.brajnovic.event.RaceCreatedEvent;
import org.brajnovic.event.RaceDeletedEvent;
import org.brajnovic.event.RaceEvent;
import org.brajnovic.event.RaceUpdatedEvent;

public class RaceEventDeserializer implements Deserializer<RaceEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public RaceEvent deserialize(String topic, byte[] data) {
        try {
            JsonNode root = objectMapper.readTree(data);
            String eventType = root.get("eventType").asText();
            if ("RACE_CREATED".equals(eventType)) {
                return objectMapper.treeToValue(root, RaceCreatedEvent.class);
            } else if ("RACE_UPDATED".equals(eventType)) {
                return objectMapper.treeToValue(root, RaceUpdatedEvent.class);
            }  else if ("RACE_DELETED".equals(eventType)) {
                return objectMapper.treeToValue(root, RaceDeletedEvent.class);
            } else {
                throw new IllegalArgumentException("Unknown eventType: " + eventType);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing RaceEvent", e);
        }
    }

}