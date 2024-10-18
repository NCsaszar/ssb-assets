package com.secure_sentinel.accounts.accounts_service.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTypeTest {

    @Test
    void testForValue() {
        // Arrange, Act and Assert
        assertThrows(IllegalArgumentException.class, () -> AccountType.forValue("42"));
        assertEquals(AccountType.CHECKING, AccountType.forValue("CHECKING"));
        assertEquals(AccountType.CREDIT, AccountType.forValue("CREDIT"));
        assertEquals(AccountType.SAVINGS, AccountType.forValue("SAVINGS"));
    }


    @Test
    void testGetValue() {
        // Arrange, Act and Assert
        assertEquals("Checking", AccountType.valueOf("CHECKING").getValue());
    }
}