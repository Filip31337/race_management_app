package org.brajnovic.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.util.UUID;

@Table("application_by_id")
public class ApplicationById {

    @PrimaryKey
    private UUID id;

    private String firstName;

    private String lastName;

    private UUID raceId;

    public ApplicationById() {
    }

    public ApplicationById(UUID id, String firstName, String lastName, UUID raceId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.raceId = raceId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UUID getRaceId() {
        return raceId;
    }

    public void setRaceId(UUID raceId) {
        this.raceId = raceId;
    }
}

