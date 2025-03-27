package org.brajnovic.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brajnovic.service.DataMigrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialMigrationScheduledTask {

    private final DataMigrationService migrationService;

    private final RestTemplate restTemplate;

    private volatile boolean migrationAttempted = false;

    @Value("${migration.trigger.health-check-url:http://query-service:8081/actuator/health}")
    private String healthCheckUrl;

    @Scheduled(fixedRateString = "${migration.trigger.fixed-delay-ms:5000}",
            initialDelayString = "${migration.trigger.initial-delay-ms:90000}")
    public void attemptMigrationWhenHealthy() {
        if (!migrationAttempted) {
            try {
                ResponseEntity<Map> response = restTemplate.getForEntity(
                        healthCheckUrl,
                        Map.class
                );

                if (response.getStatusCode().is2xxSuccessful() &&
                        "UP".equals(response.getBody().get("status"))) {

                    migrationAttempted = true;
                    log.info("Service healthy - triggering data migration");
                    migrationService.migrateAllData();
                }
            } catch (Exception e) {
                log.debug("Service not ready yet - will retry in {}ms",
                        "${migration.trigger.fixed-delay-ms:5000}");
            }
        }
    }
}
