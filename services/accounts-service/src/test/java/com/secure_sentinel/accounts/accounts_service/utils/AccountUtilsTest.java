package com.secure_sentinel.accounts.accounts_service.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountUtilsTest {
    /**
     * Method under test: {@link AccountUtils#generateAccountNumber(Integer)}
     */
    @Test
    void testGenerateAccountNumber() {
        assertNotNull(AccountUtils.generateAccountNumber(1));
    }

    /**
     * Method under test: {@link AccountUtils#maskAccountNumber(String)}
     */
    @Test
    void testMaskAccountNumber() {
        assertEquals("XXXX-XXXX-XXXX-4444", AccountUtils.maskAccountNumber("4444"));
    }
}