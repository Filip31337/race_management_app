package org.brajnovic.listener;

import lombok.extern.slf4j.Slf4j;
import org.brajnovic.event.RaceCreatedEvent;
import org.brajnovic.event.RaceDeletedEvent;
import org.brajnovic.event.RaceEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.brajnovic.entity.RaceById;
import org.brajnovic.repository.RaceByIdRepository;

@Slf4j
@Service
public class RaceEventListener {

    private final RaceByIdRepository raceByIdRepository;

    public RaceEventListener(RaceByIdRepository raceByIdRepository) {
        this.raceByIdRepository = raceByIdRepository;
    }

    @KafkaListener(topics = "race-events", groupId = "query-service")
    public void handleEvent(RaceEvent event) {
        log.info("Received event on KafkaListener, attempting to parse event.");
        if (event instanceof RaceCreatedEvent e) {
            log.info("Received RaceCreatedEvent: " + e);
            RaceById race = new RaceById(e.getRaceId(), e.getName(), e.getDistance());
            raceByIdRepository.save(race);
        } else if (event instanceof RaceDeletedEvent e) {
            log.info("Received RaceDeletedEvent: " + e);
            raceByIdRepository.deleteById(e.getRaceId());
        } else {
            log.info("Received event of unknown type. Skipping logic.Class name of received object: " + event.getClass().getName());
        }
    }
}