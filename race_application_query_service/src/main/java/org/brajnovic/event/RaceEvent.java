package org.brajnovic.event;

import java.util.UUID;

public abstract class RaceEvent {

    private UUID raceId;

    private String eventType;

    public RaceEvent() {}

    public RaceEvent(UUID raceId, String eventType) {
        this.raceId = raceId;
        this.eventType = eventType;
    }

    public UUID getRaceId() {
        return raceId;
    }

    public String getEventType() {
        return eventType;
    }
}
