package org.brajnovic.event;

import java.util.UUID;

public abstract class ApplicationEvent {

    private UUID applicationId;

    private String eventType;

    protected ApplicationEvent() {
    }

    protected ApplicationEvent(UUID applicationId, String eventType) {
        this.applicationId = applicationId;
        this.eventType = eventType;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public String getEventType() {
        return eventType;
    }
}
