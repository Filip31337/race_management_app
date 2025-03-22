package org.brajnovic.event;

import java.util.UUID;

public class RaceCreatedEvent extends RaceEvent {
    private final String name;
    private final String distance;

    public RaceCreatedEvent(UUID raceId, String name, String distance) {
        super(raceId, "RACE_CREATED");
        this.name = name;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }
}
