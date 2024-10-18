package com.smoothstack.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Inject the environment variable
    @Value("${MAIL_SERVICE_USERNAME}")
    private String email;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(email);

        mailSender.send(message);
    }

    public void sendWelcomeEmail(String toEmail, String username, String verificationLink) {
        String subject = "Welcome to Secure Sentinel Bank!";
        String body = "Dear " + username + ",\n\n"
                + "Welcome to Secure Sentinel Bank! We are excited to have you on board.\n\n"
                + "Please click the link below to verify your email address:\n\n"
                + verificationLink + "\n\n"
                + "This link will expire in 24 hours.\n\n"
                + "Best regards,\n"
                + "The Secure Sentinel Bank Team";

        sendSimpleEmail(toEmail, subject, body);
    }

    public void sendVerificationEmail(String toEmail, String verificationLink) {
        String subject = "Email Verification - Secure Sentinel Bank";
        String body = "Please click the link below to verify your email address:\n\n" + verificationLink + "\n\n"
                + "This link will expire in 24 hours.\n\n"
                + "Best regards,\n"
                + "The Secure Sentinel Bank Team";

        sendSimpleEmail(toEmail, subject, body);
    }
}
