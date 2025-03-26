package org.brajnovic.controller;

import org.brajnovic.entity.Application;
import org.brajnovic.service.ApplicationService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/command-service-api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public Application createApplication(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String club, @RequestParam UUID raceId) {
        return applicationService.createApplication(firstName, lastName, club, raceId);
    }

    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable UUID id) {
        applicationService.deleteApplication(id);
    }
}
