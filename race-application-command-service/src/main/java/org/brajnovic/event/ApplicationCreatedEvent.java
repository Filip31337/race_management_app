package org.brajnovic.event;

import java.util.UUID;

public class ApplicationCreatedEvent extends ApplicationEvent {

    private final String firstName;

    private final String lastName;

    private final UUID raceId;

    private final String userEmail;

    public ApplicationCreatedEvent(UUID applicationId, String firstName, String lastName, UUID raceId, String userEmail) {
        super(applicationId, "APPLICATION_CREATED");
        this.firstName = firstName;
        this.lastName = lastName;
        this.raceId = raceId;
        this.userEmail = userEmail;
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

    public String getUserEmail() {
        return userEmail;
    }
}
