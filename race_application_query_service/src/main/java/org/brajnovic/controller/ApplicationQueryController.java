package org.brajnovic.controller;

import org.brajnovic.entity.ApplicationById;
import org.brajnovic.repository.ApplicationByIdRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/query-service-api/applications")
public class ApplicationQueryController {

    private final ApplicationByIdRepository applicationByIdRepository;

    public ApplicationQueryController(ApplicationByIdRepository applicationByIdRepository) {
        this.applicationByIdRepository = applicationByIdRepository;
    }

    @GetMapping
    public List<ApplicationById> getAllApplications() {
        return applicationByIdRepository.findAll();
    }

    @GetMapping("/{id}")
    public ApplicationById getApplication(@PathVariable UUID id) {
        return applicationByIdRepository.findById(id).orElse(null);
    }

}
