package org.brajnovic.listener;

import lombok.extern.slf4j.Slf4j;
import org.brajnovic.entity.ApplicationById;
import org.brajnovic.event.ApplicationCreatedEvent;
import org.brajnovic.event.ApplicationDeletedEvent;
import org.brajnovic.event.ApplicationEvent;
import org.brajnovic.repository.ApplicationByIdRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApplicationEventListener {

    private final ApplicationByIdRepository applicationByIdRepository;

    public ApplicationEventListener(ApplicationByIdRepository applicationByIdRepository) {
        this.applicationByIdRepository = applicationByIdRepository;
    }

    @KafkaListener(topics = "application-events", groupId = "query-service-application",
            containerFactory = "applicationKafkaListenerContainerFactory")
    public void handleApplicationEvent(ApplicationEvent event) {
        log.info("Received application event on KafkaListener, class: {}", event.getClass().getName());
        if (event instanceof ApplicationCreatedEvent e) {
            log.info("Received ApplicationCreatedEvent: {}", e);
            ApplicationById application = new ApplicationById(
                    e.getApplicationId(),
                    e.getFirstName(),
                    e.getLastName(),
                    e.getRaceId()
            );
            applicationByIdRepository.save(application);
        } else if (event instanceof ApplicationDeletedEvent e) {
            log.info("Received ApplicationDeletedEvent: {}", e);
            applicationByIdRepository.deleteById(e.getApplicationId());
        } else {
            log.info("Received event of unknown type. Skipping logic. Class: {}", event.getClass().getName());
        }
    }

}
