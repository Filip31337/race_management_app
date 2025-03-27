package org.brajnovic.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class ApplicationCreatedEvent extends ApplicationEvent {

    private final String firstName;

    private final String lastName;

    private final UUID raceId;

    private final String userEmail;

    @JsonCreator
    public ApplicationCreatedEvent(
            @JsonProperty("applicationId") UUID applicationId,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("raceId") UUID raceId,
            @JsonProperty("userEmail") String userEmail) {
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
