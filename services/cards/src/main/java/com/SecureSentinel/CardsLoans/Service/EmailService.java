package com.SecureSentinel.CardsLoans.Service;

import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final ApiService apiService;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, ApiService apiService) {
        this.javaMailSender = javaMailSender;
        this.apiService = apiService;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.smtp.starttls.enable", "true");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom("securesentinelcardloan@gmail.com");
            message.setSubject(subject);
            message.setText(body);

            javaMailSender.send(message);

            System.out.println("Mail sent to " + to);
        } catch (MailException ex) {
            System.err.println("Failed to send email to " + to + ": " + ex.getMessage());
        }
    }

    public void sendCardConfirmationEmail(int userId) {
        try {
            JsonNode jsonNode = apiService.getUserInfo(userId);

            if (jsonNode != null && jsonNode.has("email")) {
                String to = jsonNode.get("email").asText();
                String subject = "Credit Card Confirmation!";
                String body = """
                Dear Customer,

                Congratulations on being approved for your new credit card!

                We're excited to inform you that your application has been successfully processed.
                Feel free to explore the features and benefits of your new credit card.

                If you have any questions or need assistance, please don't hesitate to contact us.

                Best regards,
                SSB Cards/Loans Department""";

                sendEmail(to, subject, body);
            } else {
                System.out.println("Email address not found for user with ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("Failed to send card confirmation email: " + e.getMessage());
        }
    }


    public void sendLoanConfirmationEmail(int userId) {
        try {
            JsonNode jsonNode = apiService.getUserInfo(userId);

            if (jsonNode != null && jsonNode.has("email")) {
                String to = jsonNode.get("email").asText();
                String subject = "Loan Application Received";
                String body = """
                Dear Customer,

                Thank you for applying for a loan with us.

                We have received your loan application and it is currently under review by one of our bankers.
                You will be notified once the review process is complete.

                If you have any questions or need further assistance, please don't hesitate to contact us.

                Best regards,
                SSB Cards/Loans Department""";

                sendEmail(to, subject, body);
            } else {
                System.out.println("Email address not found for user with ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("Failed to send loan confirmation email: " + e.getMessage());
        }
    }


    public void sendLoanApprovalEmail(int userId) {
        try {
            JsonNode jsonNode = apiService.getUserInfo(userId);

            if (jsonNode != null && jsonNode.has("email")) {
                String to = jsonNode.get("email").asText();
                String subject = "Loan Application Approved!";
                String body = """
                Dear Customer,

                Congratulations on being approved for your loan!

                We're excited to inform you that your loan application has been successfully processed and approved.
                You can now access your loan amount and use it for your financial needs.

                If you have any questions or need assistance, please don't hesitate to contact us.

                Best regards,
                SSB Cards/Loans Department""";

                sendEmail(to, subject, body);
            } else {
                System.out.println("Email address not found for user with ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("Failed to send loan approval email: " + e.getMessage());
        }
    }


    public void cardFreezeConfirmation(int userId) {
        try {
            JsonNode jsonNode = apiService.getUserInfo(userId);

            if (jsonNode != null && jsonNode.has("email")) {
                String to = jsonNode.get("email").asText();
                String subject = "Your Credit Card has been frozen!";
                String body = """
                Dear Customer,

                Your Credit Card has been frozen! If this is a mistake please contact us so we can reactivate it.

                If you have any questions or need assistance, please don't hesitate to contact us.

                Best regards,
                SSB Cards/Loans Department""";

                sendEmail(to, subject, body);
            } else {
                System.out.println("Email address not found for user with ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("Failed to send card freeze confirmation email: " + e.getMessage());
        }
    }

}
