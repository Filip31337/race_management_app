package org.brajnovic.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.brajnovic.util.SecurityUtils;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.UUID;

import static org.mockito.Mockito.mockStatic;

@Tag("integration")
@ExtendWith(MockitoExtension.class)
public abstract class BaseIntegrationTest<T> {

    protected final String testEmail = "test@example.com";

    protected final UUID testId = UUID.randomUUID();

    protected MockedStatic<SecurityUtils> securityUtilsMock;

    @Mock
    protected KafkaTemplate<String, T> kafkaTemplate;

    @BeforeEach
    void baseSetUp() {
        securityUtilsMock = mockStatic(SecurityUtils.class);
        securityUtilsMock.when(SecurityUtils::getCurrentUserEmail).thenReturn(testEmail);
    }

    @AfterEach
    void baseTearDown() {
        securityUtilsMock.close();
    }
}
