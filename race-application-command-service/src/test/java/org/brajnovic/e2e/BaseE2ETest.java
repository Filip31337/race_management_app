package org.brajnovic.e2e;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.Duration;

@Tag("e2e")
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BaseE2ETest {

	protected static int postgresPort;
	protected static int zookeeperPort;
	protected static int kafkaPort;
	protected static int cassandraPort;
	protected static int commandServicePort;
	protected static int queryServicePort;
	protected static int angularAppPort;

	protected static String commandServiceUrl;
	protected static String queryServiceUrl;
	protected static String postgresJdbcUrl;
	protected static String kafkaBootstrapServers;
	protected static String cassandraContactPoint;

	@Container
	private static ComposeContainer dockerComposeContainer = new ComposeContainer(
			new File("src/test/resources/docker-compose-test.yml"))
			.withExposedService("postgres", 5432,
					Wait.forLogMessage(".*database system is ready to accept connections.*", 1)
							.withStartupTimeout(Duration.ofMinutes(2)))
			.withExposedService("zookeeper", 2181,
					Wait.forListeningPort()
							.withStartupTimeout(Duration.ofMinutes(3)))
			.withExposedService("kafka", 9092,
					Wait.forLogMessage(".*topicName='race-events'.*", 1)
							.withStartupTimeout(Duration.ofMinutes(3)))
			.withExposedService("cassandra", 9042,
								Wait.forLogMessage(".*Starting listening for CQL clients.*", 1)
                            .withStartupTimeout(Duration.ofMinutes(2)))
			.withExposedService("command-service", 8080,
								Wait.forHttp("/actuator/health")
                            .forStatusCode(200)
                            .withStartupTimeout(Duration.ofMinutes(2)))
			.withExposedService("query-service", 8081,
								Wait.forHttp("/actuator/health")
                            .forStatusCode(200)
                            .withStartupTimeout(Duration.ofMinutes(2)))
			.withExposedService("angular-app", 80,
								Wait.forHttp("/")
                            .forStatusCode(200)
                            .withStartupTimeout(Duration.ofMinutes(2)))
			.withLocalCompose(true)
            .withEnv("KAFKA_BROKER_ID", "1")
            .withEnv("KAFKA_ZOOKEEPER_CONNECT", "zookeeper:2181")
            .withEnv("KAFKA_LISTENERS", "PLAINTEXT://0.0.0.0:9092")
            .withEnv("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:9092")

            .withLogConsumer("postgres", outputFrame -> log.info("Postgres: {}", outputFrame.getUtf8String()))
			.withLogConsumer("kafka", outputFrame -> log.info("Kafka: {}", outputFrame.getUtf8String()))
			.withLogConsumer("zookeeper", outputFrame -> log.info("Zookeeper: {}", outputFrame.getUtf8String()))
			.withLogConsumer("cassandra", outputFrame -> log.info("Cassandra: {}", outputFrame.getUtf8String()))
			.withLogConsumer("command-service", outputFrame -> log.info("Command Service: {}", outputFrame.getUtf8String()))
			.withLogConsumer("query-service", outputFrame -> log.info("Query Service: {}", outputFrame.getUtf8String()))
			.withLogConsumer("angular-app", outputFrame -> log.info("Angular App: {}", outputFrame.getUtf8String()))
			.withBuild(true);

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		postgresPort = dockerComposeContainer.getServicePort("postgres", 5432);
		zookeeperPort = dockerComposeContainer.getServicePort("zookeeper", 2181);
		kafkaPort = dockerComposeContainer.getServicePort("kafka", 9092);
		cassandraPort = dockerComposeContainer.getServicePort("cassandra", 9042);
		commandServicePort = dockerComposeContainer.getServicePort("command-service", 8080);
		queryServicePort = dockerComposeContainer.getServicePort("query-service", 8081);
		angularAppPort = dockerComposeContainer.getServicePort("angular-app", 80);


		log.info("Configuring test properties...");
		log.info("Postgres port: {}", postgresPort);
		log.info("Kafka port: {}", kafkaPort);

		commandServiceUrl = "http://localhost:" + commandServicePort;
		queryServiceUrl = "http://localhost:" + queryServicePort;
		postgresJdbcUrl = "jdbc:postgresql://localhost:" + postgresPort + "/test_race_management";
		kafkaBootstrapServers = "localhost:" + kafkaPort;
		cassandraContactPoint = "localhost:" + cassandraPort;

		registry.add("spring.datasource.url",
				() -> "jdbc:postgresql://localhost:" + postgresPort + "/test_race_management");
		registry.add("spring.datasource.username", () -> "test");
		registry.add("spring.datasource.password", () -> "test");

		registry.add("spring.kafka.bootstrap-servers",
				() -> "localhost:" + kafkaPort);

		registry.add("spring.data.cassandra.contactpoints", () -> "cassandra");
		registry.add("spring.data.cassandra.port", () -> cassandraPort);
		registry.add("spring.data.cassandra.keyspace-name", () -> "race_query");
		registry.add("spring.data.cassandra.local-datacenter", () -> "datacenter1");
		registry.add("spring.data.cassandra.username", () -> "cassandra");
		registry.add("spring.data.cassandra.password", () -> "cassandra");
		registry.add("spring.data.cassandra.schema-action", () -> "create_if_not_exists");
		registry.add("spring.data.cassandra.connect-timeout-millis", () -> "120000");
		registry.add("spring.data.cassandra.reconnection-policy", () -> "com.datastax.driver.core.policies.ConstantReconnectionPolicy");
	}

	@Test
	@Order(1)
	void contextLoads() {
		log.info("Service Ports:");
		log.info("- Postgres: {}", dockerComposeContainer.getServicePort("postgres", 5432));
		log.info("- Zookeeper: {}", dockerComposeContainer.getServicePort("zookeeper", 2181));
		log.info("- Kafka: {}", dockerComposeContainer.getServicePort("kafka", 9092));
		log.info("- Cassandra: {}", dockerComposeContainer.getServicePort("cassandra", 9042));
		log.info("- Command Service: {}", dockerComposeContainer.getServicePort("command-service", 8080));
		log.info("- Query Service: {}", dockerComposeContainer.getServicePort("query-service", 8081));
		log.info("- Angular App: {}", dockerComposeContainer.getServicePort("angular-app", 80));

		assertTrue(dockerComposeContainer.getServicePort("postgres", 5432) > 0);
		assertTrue(dockerComposeContainer.getServicePort("zookeeper", 2181) > 0);
		assertTrue(dockerComposeContainer.getServicePort("kafka", 9092) > 0);
		assertTrue(dockerComposeContainer.getServicePort("cassandra", 9042) > 0);
		assertTrue(dockerComposeContainer.getServicePort("command-service", 8080) > 0);
		assertTrue(dockerComposeContainer.getServicePort("query-service", 8081) > 0);
		assertTrue(dockerComposeContainer.getServicePort("angular-app", 80) > 0);
	}
}