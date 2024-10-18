package com.SecureSentinel.CardsLoans.Controller;

import com.SecureSentinel.CardsLoans.Service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public void testEmail(){
        emailService.sendEmail("edali80@outlook.com","test","This is a test.");
    }
}
