package com.smoothstack.userservice;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
@EnableBatchProcessing
public class UserServiceApplication {

	@Autowired
	@Lazy
	private EurekaClient eurekaClient;
	@Value("${spring.application.name}")

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}
