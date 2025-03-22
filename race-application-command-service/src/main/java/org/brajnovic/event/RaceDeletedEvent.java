package org.brajnovic.event;

import java.util.UUID;

public class RaceDeletedEvent extends RaceEvent {
    public RaceDeletedEvent(UUID raceId) {
        super(raceId, "RACE_DELETED");
    }
}
