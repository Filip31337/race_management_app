package org.brajnovic.controller;

import org.brajnovic.entity.RaceById;
import org.brajnovic.repository.RaceByIdRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/query/races")
public class RaceQueryController {

    private final RaceByIdRepository raceRepo;

    public RaceQueryController(RaceByIdRepository raceRepo) {
        this.raceRepo = raceRepo;
    }

    @GetMapping
    public List<RaceById> getAllRaces() {
        return raceRepo.findAll();
    }

    @GetMapping("/{id}")
    public RaceById getRace(@PathVariable UUID id) {
        return raceRepo.findById(id).orElse(null);
    }
}
