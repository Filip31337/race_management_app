package org.brajnovic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brajnovic.entity.Application;
import org.brajnovic.entity.Race;
import org.brajnovic.event.ApplicationCreatedEvent;
import org.brajnovic.event.ApplicationEvent;
import org.brajnovic.event.RaceCreatedEvent;
import org.brajnovic.event.RaceEvent;
import org.brajnovic.repository.ApplicationRepository;
import org.brajnovic.repository.RaceRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataMigrationService {

    private final RaceRepository raceRepository;

    private final ApplicationRepository applicationRepository;

    private final KafkaTemplate<String, RaceEvent> raceEventKafkaTemplate;

    private final KafkaTemplate<String, ApplicationEvent> applicationEventKafkaTemplate;

    @Transactional
    public void migrateAllData() {
        log.info("Starting data migration to Cassandra via Kafka events");

        try {
            // 1. Migrate races
            List<Race> races = raceRepository.findAll();
            log.info("Found {} races to migrate", races.size());

            races.forEach(race -> {
                raceEventKafkaTemplate.send("race-events",
                        new RaceCreatedEvent(
                                race.getId(),
                                race.getName(),
                                race.getDistance().toString()
                        )
                );
            });

            List<Application> applications = applicationRepository.findAll();
            log.info("Found {} applications to migrate", applications.size());

            applications.forEach(app -> {
                applicationEventKafkaTemplate.send("application-events",
                        new ApplicationCreatedEvent(
                                app.getId(),
                                app.getFirstName(),
                                app.getLastName(),
                                app.getRaceId(),
                                app.getUserEmail()
                        )
                );
            });

            log.info("Data migration completed successfully");
        } catch (Exception e) {
            log.error("Data migration failed", e);
        }
    }
}
