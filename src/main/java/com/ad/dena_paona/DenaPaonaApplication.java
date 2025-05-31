package com.ad.dena_paona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // Add this import

@SpringBootApplication
@EnableScheduling // Add this annotation
public class DenaPaonaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DenaPaonaApplication.class, args);
	}

}
