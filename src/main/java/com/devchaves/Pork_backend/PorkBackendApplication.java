package com.devchaves.Pork_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PorkBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PorkBackendApplication.class, args);
	}

}
