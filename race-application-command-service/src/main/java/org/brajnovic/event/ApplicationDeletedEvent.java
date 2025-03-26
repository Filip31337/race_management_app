package org.brajnovic.event;

import java.util.UUID;

public class ApplicationDeletedEvent extends ApplicationEvent {

    public ApplicationDeletedEvent(UUID applicationId) {
        super(applicationId, "APPLICATION_DELETED");
    }
}
