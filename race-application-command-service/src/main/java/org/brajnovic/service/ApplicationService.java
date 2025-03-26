package org.brajnovic.service;

import org.brajnovic.entity.Application;
import org.brajnovic.event.ApplicationCreatedEvent;
import org.brajnovic.event.ApplicationDeletedEvent;
import org.brajnovic.event.ApplicationEvent;
import org.brajnovic.repository.ApplicationRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final KafkaTemplate<String, ApplicationEvent> kafkaTemplate;

    public ApplicationService(ApplicationRepository applicationRepository,
                              KafkaTemplate<String, ApplicationEvent> kafkaTemplate) {
        this.applicationRepository = applicationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Application createApplication(String firstName, String lastName, String club, UUID raceId) {
        Application application = new Application();
        application.setId(UUID.randomUUID());
        application.setFirstName(firstName);
        application.setLastName(lastName);
        application.setClub(club);
        application.setRaceId(raceId);
        applicationRepository.save(application);

        ApplicationCreatedEvent event = new ApplicationCreatedEvent(
                application.getId(),
                application.getFirstName(),
                application.getLastName(),
                application.getRaceId()
        );
        kafkaTemplate.send("application-events", event);

        return application;
    }

    public void deleteApplication(UUID id) {

        applicationRepository.deleteById(id);

        ApplicationDeletedEvent event = new ApplicationDeletedEvent(id);
        kafkaTemplate.send("application-events", event);
    }
}
