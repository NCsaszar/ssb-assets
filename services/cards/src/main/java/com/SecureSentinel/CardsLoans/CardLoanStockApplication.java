package com.SecureSentinel.CardsLoans;

import com.SecureSentinel.CardsLoans.Mapper.MapperConfig;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class CardLoanStockApplication {
	public static void main(String[] args) {
		SpringApplication.run(CardLoanStockApplication.class, args);
	}
}
