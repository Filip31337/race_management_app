package org.brajnovic.event;

import java.util.UUID;

public class ApplicationCreatedEvent extends ApplicationEvent {

    private final String firstName;

    private final String lastName;

    private final UUID raceId;

    public ApplicationCreatedEvent(UUID applicationId, String firstName, String lastName, UUID raceId) {
        super(applicationId, "APPLICATION_CREATED");
        this.firstName = firstName;
        this.lastName = lastName;
        this.raceId = raceId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UUID getRaceId() {
        return raceId;
    }
}
