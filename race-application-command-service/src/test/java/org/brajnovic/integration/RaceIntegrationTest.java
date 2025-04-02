package org.brajnovic.integration;

import org.brajnovic.entity.Race;
import org.brajnovic.entity.RaceDistance;
import org.brajnovic.event.*;
import org.brajnovic.repository.RaceRepository;
import org.brajnovic.service.RaceService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("integration")
public class RaceIntegrationTest extends BaseIntegrationTest<RaceEvent> {

    @Mock
    private RaceRepository raceRepository;

    @InjectMocks
    private RaceService raceService;

    @Captor
    private ArgumentCaptor<Race> raceCaptor;

    @Captor
    private ArgumentCaptor<RaceCreatedEvent> createdEventCaptor;

    @Captor
    private ArgumentCaptor<RaceUpdatedEvent> updatedEventCaptor;

    @Captor
    private ArgumentCaptor<RaceDeletedEvent> deletedEventCaptor;

    @Test
    void createRace_shouldSaveRaceAndSendEvent() {
        // Given
        String name = "Marathon";
        RaceDistance distance = RaceDistance.MARATHON;

        // Configure the mock to return the race it receives
        when(raceRepository.save(any(Race.class))).thenAnswer(invocation -> {
            Race race = invocation.getArgument(0);
            return race;
        });

        // When
        Race result = raceService.createRace(name, distance);

        // Then
        verify(raceRepository).save(raceCaptor.capture());
        Race savedRace = raceCaptor.getValue();

        assertAll(
                () -> assertNotNull(savedRace.getId()),
                () -> assertEquals(name, savedRace.getName()),
                () -> assertEquals(distance, savedRace.getDistance())
        );

        verify(kafkaTemplate).send(eq("race-events"), createdEventCaptor.capture());
        RaceCreatedEvent event = createdEventCaptor.getValue();

        assertAll(
                () -> assertEquals(savedRace.getId(), event.getRaceId()),
                () -> assertEquals(name, event.getName()),
                () -> assertEquals(distance.name(), event.getDistance()),
                () -> assertEquals("RACE_CREATED", event.getEventType())
        );

        assertSame(savedRace, result);
    }

    @Test
    void updateRace_shouldUpdateExistingRace() {
        // Given
        UUID raceId = UUID.randomUUID();
        Race existingRace = new Race();
        existingRace.setId(raceId);
        existingRace.setName("Old Name");
        existingRace.setDistance(RaceDistance.HALF_MARATHON);

        when(raceRepository.findById(raceId)).thenReturn(Optional.of(existingRace));
        when(raceRepository.save(any(Race.class))).thenAnswer(invocation ->
                invocation.getArgument(0));

        // When
        Race result = raceService.updateRace(raceId, "New Name", RaceDistance.MARATHON);

        // Then
        verify(raceRepository).save(raceCaptor.capture());
        Race updatedRace = raceCaptor.getValue();

        assertAll(
                () -> assertEquals(raceId, updatedRace.getId()),
                () -> assertEquals("New Name", updatedRace.getName()),
                () -> assertEquals(RaceDistance.MARATHON, updatedRace.getDistance())
        );

        verify(kafkaTemplate).send(eq("race-events"), updatedEventCaptor.capture());
        RaceUpdatedEvent event = updatedEventCaptor.getValue();

        assertAll(
                () -> assertEquals(raceId, event.getRaceId()),
                () -> assertEquals("New Name", event.getName()),
                () -> assertEquals(RaceDistance.MARATHON.name(), event.getDistance()),
                () -> assertEquals("RACE_UPDATED", event.getEventType())
        );
    }

    @Test
    void deleteRace_shouldDeleteAndSendEvent() {
        // Given
        UUID raceId = UUID.randomUUID();

        // When
        raceService.deleteRace(raceId);

        // Then
        verify(raceRepository).deleteById(raceId);
        verify(kafkaTemplate).send(eq("race-events"), deletedEventCaptor.capture());

        RaceDeletedEvent event = deletedEventCaptor.getValue();
        assertEquals(raceId, event.getRaceId());
        assertEquals("RACE_DELETED", event.getEventType());
    }
}
