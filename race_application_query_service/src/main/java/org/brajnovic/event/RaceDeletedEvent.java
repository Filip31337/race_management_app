package org.brajnovic.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class RaceDeletedEvent extends RaceEvent {

    @JsonCreator
    public RaceDeletedEvent(@JsonProperty("raceId") UUID raceId) {
        super(raceId, "RACE_DELETED");
    }
}
