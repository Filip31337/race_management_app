package org.brajnovic.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.brajnovic.entity.RaceDistance;
import org.brajnovic.entity.Race;
import org.brajnovic.service.RaceService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/command-service-api/races")
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @PostMapping
    public Race createRace(@RequestParam String name, @RequestParam RaceDistance distance) {
        return raceService.createRace(name, distance);
    }

    @PutMapping("/{id}")
    public Race updateRace(@PathVariable UUID id, @RequestParam String name, @RequestParam RaceDistance distance) {
        return raceService.updateRace(id, name, distance);
    }

    @DeleteMapping("/{id}")
    public void deleteRace(@PathVariable UUID id) {
        raceService.deleteRace(id);
    }
}
