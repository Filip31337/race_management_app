package org.brajnovic.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class ApplicationDeletedEvent extends ApplicationEvent {

    @JsonCreator
    public ApplicationDeletedEvent(@JsonProperty("applicationId") UUID applicationId) {
        super(applicationId, "APPLICATION_DELETED");
    }
}
