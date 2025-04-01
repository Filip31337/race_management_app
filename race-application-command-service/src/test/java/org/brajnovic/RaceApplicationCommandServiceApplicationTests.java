package org.brajnovic;

import org.junit.jupiter.api.Test;
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

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
class RaceApplicationCommandServiceApplicationTests {

	@Container
	private static ComposeContainer dockerComposeContainer = new ComposeContainer(
		new File("src/test/resources/docker-compose-test.yml"))
		.withExposedService("postgres", 5432, Wait.forListeningPort())
		.withExposedService("zookeeper", 2181, Wait.forListeningPort())
		.withExposedService("kafka", 9092, Wait.forListeningPort())
		.withExposedService("cassandra", 9042, Wait.forListeningPort())
		.withExposedService("command-service", 8080, Wait.forListeningPort())
		.withExposedService("query-service", 8081, Wait.forListeningPort())
		.withExposedService("angular-app", 80, Wait.forListeningPort())
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
	protected static void configureProperties(DynamicPropertyRegistry registry) {
		log.info("Configuring test properties...");
		String postgresPort = String.valueOf(dockerComposeContainer.getServicePort("postgres", 5432));
		String kafkaPort = String.valueOf(dockerComposeContainer.getServicePort("kafka", 9092));
		
		log.info("Postgres port: {}", postgresPort);
		log.info("Kafka port: {}", kafkaPort);

		registry.add("spring.datasource.url", () -> 
			String.format("jdbc:postgresql://localhost:%s/test_race_management", postgresPort));
		registry.add("spring.datasource.username", () -> "test");
		registry.add("spring.datasource.password", () -> "test");
		registry.add("spring.kafka.bootstrap-servers", () -> 
			String.format("localhost:%s", kafkaPort));
	}

	@Test
	void contextLoads() {
		log.info("Starting context load test...");
		int postgresPort = dockerComposeContainer.getServicePort("postgres", 5432);
		int zookeeperPort = dockerComposeContainer.getServicePort("zookeeper", 2181);
		int kafkaPort = dockerComposeContainer.getServicePort("kafka", 9092);
		int cassandraPort = dockerComposeContainer.getServicePort("cassandra", 9042);
		int commandServicePort = dockerComposeContainer.getServicePort("command-service", 8080);
		int queryServicePort = dockerComposeContainer.getServicePort("query-service", 8081);
		int angularAppPort = dockerComposeContainer.getServicePort("angular-app", 80);
		
		log.info("Postgres port: {}", postgresPort);
		log.info("Zookeeper port: {}", zookeeperPort);
		log.info("Kafka port: {}", kafkaPort);
		log.info("Cassandra port: {}", cassandraPort);
		log.info("Command Service port: {}", commandServicePort);
		log.info("Query Service port: {}", queryServicePort);
		log.info("Angular App port: {}", angularAppPort);

		assertTrue(postgresPort > 0);
		assertTrue(zookeeperPort > 0);
		assertTrue(kafkaPort > 0);
		assertTrue(cassandraPort > 0);
		assertTrue(commandServicePort > 0);
		assertTrue(queryServicePort > 0);
		assertTrue(angularAppPort > 0);
	}
}
