package com.secure_sentinel.accounts.accounts_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AccountsServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(AccountsServiceApplication.class, args);
    }
}