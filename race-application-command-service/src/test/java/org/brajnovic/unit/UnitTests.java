package org.brajnovic.unit;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.brajnovic.service.ApplicationService;
import org.brajnovic.service.RaceService;
import org.brajnovic.entity.Race;
import org.brajnovic.entity.Application;
import org.brajnovic.entity.RaceDistance;
import org.brajnovic.repository.ApplicationRepository;
import org.brajnovic.repository.RaceRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

@Tag("unit")
class UnitTests extends BaseUnitTest {

    @Mock
    private ApplicationService applicationService;

    @Mock
    private RaceService raceService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void testCreateRace() {
        // Given
        String raceName = "Test Race";
        RaceDistance distance = RaceDistance.MARATHON;
        Race expectedRace = new Race();
        expectedRace.setId(UUID.randomUUID());
        expectedRace.setName(raceName);
        expectedRace.setDistance(distance);

        when(raceService.createRace(raceName, distance)).thenReturn(expectedRace);

        // When
        Race createdRace = raceService.createRace(raceName, distance);

        // Then
        assertNotNull(createdRace);
        assertNotNull(createdRace.getId());
        assertEquals(raceName, createdRace.getName());
        assertEquals(distance, createdRace.getDistance());
        verify(raceService).createRace(raceName, distance);
    }

    @Test
    void testUpdateRace() {
        // Given
        UUID raceId = UUID.randomUUID();
        String updatedName = "Updated Race";
        RaceDistance updatedDistance = RaceDistance.HALF_MARATHON;
        Race expectedRace = new Race();
        expectedRace.setId(raceId);
        expectedRace.setName(updatedName);
        expectedRace.setDistance(updatedDistance);

        when(raceService.updateRace(raceId, updatedName, updatedDistance)).thenReturn(expectedRace);

        // When
        Race updatedRace = raceService.updateRace(raceId, updatedName, updatedDistance);

        // Then
        assertNotNull(updatedRace);
        assertEquals(raceId, updatedRace.getId());
        assertEquals(updatedName, updatedRace.getName());
        assertEquals(updatedDistance, updatedRace.getDistance());
        verify(raceService).updateRace(raceId, updatedName, updatedDistance);
    }

    @Test
    void testUpdateNonExistentRace() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        String name = "Test Race";
        RaceDistance distance = RaceDistance.MARATHON;

        when(raceService.updateRace(nonExistentId, name, distance)).thenReturn(null);

        // When
        Race result = raceService.updateRace(nonExistentId, name, distance);

        // Then
        assertNull(result);
        verify(raceService).updateRace(nonExistentId, name, distance);
    }

    @Test
    void testDeleteRace() {
        // Given
        UUID raceId = UUID.randomUUID();

        // When
        raceService.deleteRace(raceId);

        // Then
        verify(raceService).deleteRace(raceId);
    }

    @Test
    void testCreateApplication() {
        // Given
        UUID raceId = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        String club = "Test Club";
        Application expectedApplication = new Application();
        expectedApplication.setId(UUID.randomUUID());
        expectedApplication.setFirstName(firstName);
        expectedApplication.setLastName(lastName);
        expectedApplication.setClub(club);
        expectedApplication.setRaceId(raceId);

        when(applicationService.createApplication(firstName, lastName, club, raceId))
                .thenReturn(expectedApplication);

        // When
        Application application = applicationService.createApplication(firstName, lastName, club, raceId);

        // Then
        assertNotNull(application);
        assertNotNull(application.getId());
        assertEquals(firstName, application.getFirstName());
        assertEquals(lastName, application.getLastName());
        assertEquals(club, application.getClub());
        assertEquals(raceId, application.getRaceId());
        verify(applicationService).createApplication(firstName, lastName, club, raceId);
    }

    @Test
    void testDeleteApplication() {
        // Given
        UUID applicationId = UUID.randomUUID();

        // When
        applicationService.deleteApplication(applicationId);

        // Then
        verify(applicationService).deleteApplication(applicationId);
    }

    @Test
    void testCreateApplicationWithNonExistentRace() {
        // Given
        UUID nonExistentRaceId = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        String club = "Test Club";
        Application expectedApplication = new Application();
        expectedApplication.setId(UUID.randomUUID());
        expectedApplication.setFirstName(firstName);
        expectedApplication.setLastName(lastName);
        expectedApplication.setClub(club);
        expectedApplication.setRaceId(nonExistentRaceId);

        when(applicationService.createApplication(firstName, lastName, club, nonExistentRaceId))
                .thenReturn(expectedApplication);

        // When
        Application application = applicationService.createApplication(
                firstName,
                lastName,
                club,
                nonExistentRaceId
        );

        // Then
        assertNotNull(application);
        assertNotNull(application.getId());
        assertEquals(firstName, application.getFirstName());
        assertEquals(lastName, application.getLastName());
        assertEquals(club, application.getClub());
        assertEquals(nonExistentRaceId, application.getRaceId());
        verify(applicationService).createApplication(firstName, lastName, club, nonExistentRaceId);
    }
}