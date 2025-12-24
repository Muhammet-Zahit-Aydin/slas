package com.example.slas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SlasApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlasApplication.class, args);
	}

}
