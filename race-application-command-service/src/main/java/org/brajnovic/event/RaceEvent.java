package org.brajnovic.event;

import java.util.UUID;

public abstract class RaceEvent {
    private final UUID raceId;
    private final String eventType;

    protected RaceEvent(UUID raceId, String eventType) {
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
