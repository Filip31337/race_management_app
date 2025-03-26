package org.brajnovic.event;

import java.util.UUID;

public class RaceUpdatedEvent extends RaceEvent {

    private final String name;

    private final String distance;

    public RaceUpdatedEvent(UUID raceId, String name, String distance) {
        super(raceId, "RACE_UPDATED");
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
