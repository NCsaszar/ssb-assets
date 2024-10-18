package com.secure_sentinel.accounts.accounts_service.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTypeTest {

    @Test
    void testForValue() {
        // Arrange, Act and Assert
        assertThrows(IllegalArgumentException.class, () -> TransactionType.forValue("42"));
        assertEquals(TransactionType.DEPOSIT, TransactionType.forValue("DEPOSIT"));
        assertEquals(TransactionType.PAYMENT, TransactionType.forValue("PAYMENT"));
        assertEquals(TransactionType.TRANSFER, TransactionType.forValue("TRANSFER"));
        assertEquals(TransactionType.WITHDRAWAL, TransactionType.forValue("WITHDRAWAL"));
    }


    @Test
    void testGetValue() {
        // Arrange, Act and Assert
        assertEquals("Deposit", TransactionType.valueOf("DEPOSIT").getValue());
    }
}