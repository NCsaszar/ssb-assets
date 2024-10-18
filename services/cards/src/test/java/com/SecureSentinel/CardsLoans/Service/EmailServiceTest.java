package com.SecureSentinel.CardsLoans.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.SecureSentinel.CardsLoans.Service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private ApiService apiService;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        emailService.sendEmail(to, subject, body);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendCardConfirmationEmail() {
        int userId = 653;
        JsonNode jsonNode = createMockJsonNode("test@example.com");
        when(apiService.getUserInfo(userId)).thenReturn(jsonNode);

        emailService.sendCardConfirmationEmail(userId);

        verify(apiService, times(1)).getUserInfo(userId);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendLoanConfirmationEmail() {
        int userId = 653;
        JsonNode jsonNode = createMockJsonNode("test@example.com");
        when(apiService.getUserInfo(userId)).thenReturn(jsonNode);

        emailService.sendLoanConfirmationEmail(userId);

        verify(apiService, times(1)).getUserInfo(userId);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendLoanApprovalEmail() {
        int userId = 123;
        JsonNode jsonNode = createMockJsonNode("test@example.com");
        when(apiService.getUserInfo(userId)).thenReturn(jsonNode);

        emailService.sendLoanApprovalEmail(userId);

        verify(apiService, times(1)).getUserInfo(userId);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testCardFreezeConfirmation() {
        int userId = 123;
        JsonNode jsonNode = createMockJsonNode("test@example.com");
        when(apiService.getUserInfo(userId)).thenReturn(jsonNode);

        emailService.cardFreezeConfirmation(userId);

        verify(apiService, times(1)).getUserInfo(userId);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    public static JsonNode createMockJsonNode(String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.createObjectNode().put("email", email);
    }
}
