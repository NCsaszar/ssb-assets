package com.smoothstack.branchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BranchServiceApplication {

//	@Autowired
//	@Lazy
//	private EurekaClient eurekaClient;

	public static void main(String[] args) {
		SpringApplication.run(BranchServiceApplication.class, args);
	}

}
