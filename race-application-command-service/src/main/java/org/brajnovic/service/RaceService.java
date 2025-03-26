package org.brajnovic.service;

import org.brajnovic.entity.Race;
import org.brajnovic.entity.RaceDistance;
import org.brajnovic.event.RaceCreatedEvent;
import org.brajnovic.event.RaceDeletedEvent;
import org.brajnovic.event.RaceEvent;
import org.brajnovic.event.RaceUpdatedEvent;
import org.brajnovic.repository.RaceRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class RaceService {

    private final RaceRepository raceRepository;

    private final KafkaTemplate<String, RaceEvent> kafkaTemplate;

    public RaceService(RaceRepository raceRepository, KafkaTemplate<String, RaceEvent> kafkaTemplate) {
        this.raceRepository = raceRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Race createRace(String name, RaceDistance distance) {
        Race race = new Race();
        race.setId(UUID.randomUUID());
        race.setName(name);
        race.setDistance(distance);
        Race saved = raceRepository.save(race);

        RaceCreatedEvent event = new RaceCreatedEvent(
                saved.getId(),
                saved.getName(),
                saved.getDistance().name()
        );
        kafkaTemplate.send("race-events", event);

        return saved;
    }

    public Race updateRace(UUID id, String name, RaceDistance distance) {
        Optional<Race> existing = raceRepository.findById(id);
        if (existing.isEmpty()) {
            return null;
        }
        Race race = existing.get();
        race.setName(name);
        race.setDistance(distance);
        Race updated = raceRepository.save(race);

        RaceUpdatedEvent event = new RaceUpdatedEvent(
                updated.getId(),
                updated.getName(),
                updated.getDistance().name()
        );
        kafkaTemplate.send("race-events", event);

        return updated;
    }

    public void deleteRace(UUID id) {
        raceRepository.deleteById(id);
        RaceDeletedEvent event = new RaceDeletedEvent(id);
        kafkaTemplate.send("race-events", event);
    }
}
