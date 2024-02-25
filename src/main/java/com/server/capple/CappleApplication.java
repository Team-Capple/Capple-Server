package com.server.capple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CappleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CappleApplication.class, args);
	}

}
