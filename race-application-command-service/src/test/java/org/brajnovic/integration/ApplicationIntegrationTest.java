package org.brajnovic.integration;

import org.brajnovic.entity.Application;
import org.brajnovic.event.*;
import org.brajnovic.repository.ApplicationRepository;
import org.brajnovic.service.ApplicationService;
import org.brajnovic.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ApplicationIntegrationTest extends BaseIntegrationTest<ApplicationEvent> {


    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Captor
    private ArgumentCaptor<Application> applicationCaptor;

    @Captor
    private ArgumentCaptor<ApplicationCreatedEvent> createdEventCaptor;

    @Captor
    private ArgumentCaptor<ApplicationDeletedEvent> deletedEventCaptor;

    @Test
    void createApplication_savesEntityWithCorrectData() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        String club = "Running Club";
        UUID raceId = UUID.randomUUID();

        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Application result = applicationService.createApplication(
                firstName, lastName, club, raceId
        );

        // Then
        verify(applicationRepository).save(applicationCaptor.capture());
        assertThat(applicationCaptor.getValue())
                .extracting(
                        Application::getFirstName,
                        Application::getLastName,
                        Application::getClub,
                        Application::getRaceId,
                        Application::getUserEmail
                ).containsExactly(
                        firstName, lastName, club, raceId, testEmail
                );

        // Then
        verify(kafkaTemplate).send(eq("application-events"), createdEventCaptor.capture());
        assertThat(createdEventCaptor.getValue())
                .extracting(
                        ApplicationCreatedEvent::getApplicationId,
                        ApplicationCreatedEvent::getFirstName,
                        ApplicationCreatedEvent::getLastName,
                        ApplicationCreatedEvent::getRaceId,
                        ApplicationCreatedEvent::getUserEmail,
                        ApplicationCreatedEvent::getEventType
                ).containsExactly(
                        result.getId(), firstName, lastName, raceId, testEmail, "APPLICATION_CREATED"
                );
    }

    @Test
    void createApplication_usesCurrentUserEmail() {
        // Given
        String expectedEmail = "admin@test.com";
        securityUtilsMock.when(SecurityUtils::getCurrentUserEmail).thenReturn(expectedEmail);
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        applicationService.createApplication("John", "Doe", "Club", UUID.randomUUID());

        // Then
        verify(applicationRepository).save(applicationCaptor.capture());
        assertThat(applicationCaptor.getValue().getUserEmail())
                .isEqualTo(expectedEmail);
    }

    @Test
    void deleteApplication_sendsEventAfterSuccessfulDeletion() {
        // Given
        UUID applicationId = UUID.randomUUID();
        doNothing().when(applicationRepository).deleteById(applicationId);

        // When
        applicationService.deleteApplication(applicationId);

        // Then
        verify(applicationRepository).deleteById(applicationId);
        verify(kafkaTemplate).send(eq("application-events"), deletedEventCaptor.capture());
        assertThat(deletedEventCaptor.getValue())
                .extracting(
                        ApplicationDeletedEvent::getApplicationId,
                        ApplicationDeletedEvent::getEventType
                ).containsExactly(
                        applicationId, "APPLICATION_DELETED"
                );
    }
}