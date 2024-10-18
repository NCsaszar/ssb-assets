package com.secure_sentinel.accounts.accounts_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RetryUtility.class})
@ExtendWith(SpringExtension.class)
class RetryUtilityTest {
    @Autowired
    private RetryUtility retryUtility;


    @Test
    void testPerformWithRetry() {
        // Arrange
        Runnable operation = mock(Runnable.class);
        doNothing().when(operation).run();

        // Act
        retryUtility.performWithRetry(operation, 3);

        // Assert that nothing has changed
        verify(operation).run();
    }

    @Test
    void testPerformWithRetry2() {
        // Arrange
        Runnable operation = mock(Runnable.class);
        doThrow(new PessimisticLockingFailureException("Msg")).when(operation).run();

        // Act and Assert
        assertThrows(PessimisticLockingFailureException.class, () -> retryUtility.performWithRetry(operation, 3));
        verify(operation, atLeast(1)).run();
    }

    @Test
    void testPerformWithRetry3() {
        // Arrange
        Supplier<Object> operation = mock(Supplier.class);
        when(operation.get()).thenReturn("Get");

        // Act
        Object actualPerformWithRetryResult = retryUtility.performWithRetry(operation, 3);

        // Assert
        verify(operation).get();
        assertEquals("Get", actualPerformWithRetryResult);
    }


    @Test
    void testPerformWithRetry4() {
        // Arrange
        Supplier<Object> operation = mock(Supplier.class);
        when(operation.get()).thenThrow(new PessimisticLockingFailureException("Msg"));

        // Act and Assert
        assertThrows(PessimisticLockingFailureException.class, () -> retryUtility.performWithRetry(operation, 3));
        verify(operation, atLeast(1)).get();
    }
}