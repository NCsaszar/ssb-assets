package com.smoothstack.microservices;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    private static final Logger logger = LoggerFactory.getLogger(SpringApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        logger.info("This is an info message");
        //klasdjf
    }
}