package org.brajnovic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RaceApplicationCommandServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaceApplicationCommandServiceApplication.class, args);
	}

}
