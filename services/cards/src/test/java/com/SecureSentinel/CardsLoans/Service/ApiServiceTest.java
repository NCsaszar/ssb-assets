package com.SecureSentinel.CardsLoans.Service;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ApiServiceTest {

    @InjectMocks
    private ApiService apiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTopGainersAndLosers() {
        JsonNode response = apiService.getTopGainersAndLosers();
        assertNotNull(response);
    }

    @Test
    void getCompanyInfo() {
        String symbol = "IBM";
        JsonNode response = apiService.getCompanyInfo(symbol);
        assertNotNull(response);
    }

    @Test
    void getStockQuote() {
        String symbol = "IBM";
        JsonNode response = apiService.getStockQuote(symbol);
        assertNotNull(response);
    }

    @Test
    void getMonthlyInfo() {
        String symbol = "IBM";
        JsonNode response = apiService.getMonthlyInfo(symbol);
        assertNotNull(response);
    }

    @Test
    void depositMoney() {
    }

    @Test
    void transferMoney() {
    }

    @Test
    void getUserInfo() {
        Integer userId = 653;
        JsonNode response = apiService.getUserInfo(userId);
        assertNotNull(response);
    }
}