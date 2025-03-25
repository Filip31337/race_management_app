package org.brajnovic.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class RaceCreatedEvent extends RaceEvent {

    private final String name;

    private final String distance;

    @JsonCreator
    public RaceCreatedEvent(@JsonProperty("raceId") UUID raceId,
                            @JsonProperty("name") String name,
                            @JsonProperty("distance") String distance) {
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
