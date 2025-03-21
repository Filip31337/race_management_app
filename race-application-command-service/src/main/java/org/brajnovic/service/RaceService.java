package org.brajnovic.service;

import org.brajnovic.entity.Race;
import org.brajnovic.entity.RaceDistance;
import org.brajnovic.repository.RaceRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class RaceService {
    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public Race createRace(String name, RaceDistance distance) {
        Race race = new Race();
        race.setId(UUID.randomUUID());
        race.setName(name);
        race.setDistance(distance);
        return raceRepository.save(race);
    }

    public Race updateRace(UUID id, String name, RaceDistance distance) {
        Optional<Race> existing = raceRepository.findById(id);
        if (existing.isEmpty()) {
            return null;
        }
        Race race = existing.get();
        race.setName(name);
        race.setDistance(distance);
        return raceRepository.save(race);
    }

    public void deleteRace(UUID id) {
        raceRepository.deleteById(id);
    }
}
