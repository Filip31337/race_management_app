package org.brajnovic.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.util.UUID;

@Table("race_by_id")
public class RaceById {

    @PrimaryKey
    private UUID id;

    private String name;

    private String distance;

    public RaceById() {}

    public RaceById(UUID id, String name, String distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
