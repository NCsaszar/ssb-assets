package com.secure_sentinel.accounts.accounts_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiErrorTest {
    /**
     * Method under test: {@link ApiError#ApiError(HttpStatus, String)}
     */
    @Test
    void testNewApiError() {
        ApiError actualApiError = new ApiError(HttpStatus.CONTINUE, "Not all who wander are lost");

        assertEquals("Not all who wander are lost", actualApiError.getMessage());
        assertNull(actualApiError.getErrorDetails());
        assertEquals(HttpStatus.CONTINUE, actualApiError.getStatus());
    }

    /**
     * Method under test: {@link ApiError#ApiError(HttpStatus, String, Map)}
     */
    @Test
    void testNewApiError2() {
        HashMap<String, Object> errorDetails = new HashMap<>();
        ApiError actualApiError = new ApiError(HttpStatus.CONTINUE, "Not all who wander are lost", errorDetails);

        assertEquals("Not all who wander are lost", actualApiError.getMessage());
        assertEquals(HttpStatus.CONTINUE, actualApiError.getStatus());
        Map<String, Object> errorDetails2 = actualApiError.getErrorDetails();
        assertTrue(errorDetails2.isEmpty());
        assertSame(errorDetails, errorDetails2);
    }
}