package com.poc.ilovegithub;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class IlovegithubApplication {

	public static void main(String[] args) {
		SpringApplication.run(IlovegithubApplication.class, args);
	}

}
