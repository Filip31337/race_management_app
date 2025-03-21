package org.brajnovic.service;

import org.brajnovic.entity.Application;
import org.brajnovic.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application createApplication(String firstName, String lastName, String club, UUID raceId) {
        Application application = new Application();
        application.setId(UUID.randomUUID());
        application.setFirstName(firstName);
        application.setLastName(lastName);
        application.setClub(club);
        application.setRaceId(raceId);
        return applicationRepository.save(application);
    }

    public void deleteApplication(UUID id) {
        applicationRepository.deleteById(id);
    }
}
