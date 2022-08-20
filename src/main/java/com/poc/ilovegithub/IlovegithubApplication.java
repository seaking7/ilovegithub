package com.poc.ilovegithub;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@EnableBatchProcessing
@SpringBootApplication
public class IlovegithubApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(IlovegithubApplication.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);

//		SpringApplication.run(IlovegithubApplication.class, args);
	}

}
