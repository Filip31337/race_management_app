package org.brajnovic.e2e;

import lombok.extern.slf4j.Slf4j;
import org.brajnovic.dto.AuthRequest;
import org.brajnovic.dto.AuthResponse;
import org.brajnovic.entity.Race;
import org.brajnovic.entity.RaceDistance;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SmokeTest extends BaseE2ETest {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    protected static String adminToken;

    protected static String userToken;

    protected static UUID createdRaceId;

    @Test
    @Order(2)
    void adminLogin_shouldReturnValidJwt() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setEmail("admin@admin.com");
        request.setPassword("admin");

        // When
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                this.commandServiceUrl + "/auth/login",
                request,
                AuthResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isNotBlank();

        adminToken = response.getBody().getToken();
    }

    @Test
    @Order(3)
    void userLogin_shouldReturnValidJwt() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setEmail("user@user.com");
        request.setPassword("user");

        // When
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                this.commandServiceUrl + "/auth/login",
                request,
                AuthResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isNotBlank();

        userToken = response.getBody().getToken();
    }

    @Test
    @Order(4)
    void invalidLogin_shouldReturnUnauthorized() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setEmail("wrong@user.com");
        request.setPassword("wrongpassword");

        // When
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                this.commandServiceUrl + "/auth/login",
                request,
                AuthResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(5)
    void createRace_asAdmin_shouldSucceed() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        String url = this.commandServiceUrl
                + "/command-service-api/races?name=E2E_Test_Race&distance="
                + RaceDistance.HALF_MARATHON.name();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Race> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Race.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("E2E_Test_Race");
        assertThat(response.getBody().getDistance()).isEqualTo(RaceDistance.HALF_MARATHON);

        createdRaceId = response.getBody().getId();
        log.info("Created race with ID: {}", createdRaceId);
    }

    @Test
    @Order(6)
    void getRace_byId_fromQueryService_shouldReturnSameRace() throws InterruptedException {
        Thread.sleep(10000);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        String url = this.queryServiceUrl + "/query-service-api/races/" + createdRaceId;

        ResponseEntity<Race> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Race.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdRaceId);
        assertThat(response.getBody().getName()).isEqualTo("E2E_Test_Race");
        assertThat(response.getBody().getDistance()).isEqualTo(RaceDistance.HALF_MARATHON);
    }

    @Test
    @Order(7)
    void getRaces_fromQueryService_shouldReturnSomeResults() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        String url = this.queryServiceUrl + "/query-service-api/races";
        log.info("Preparing to call API on URL: " + url);

        ResponseEntity<Race[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Race[].class
        );

        log.info("Response http status code: " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Race[] races = response.getBody();
        assertThat(races).hasSize(3);
    }

    @Test
    @Order(8)
    @Disabled
    void createTimeForPostman() throws InterruptedException {
        Thread.sleep(15 * 60_000);

        assertThat("bla").isEqualTo("bla");
    }
}