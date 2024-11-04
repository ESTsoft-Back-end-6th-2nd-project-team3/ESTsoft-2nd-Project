package com.estsoft.estsoft2ndproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EsTsoft2ndProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsTsoft2ndProjectApplication.class, args);
	}

}
