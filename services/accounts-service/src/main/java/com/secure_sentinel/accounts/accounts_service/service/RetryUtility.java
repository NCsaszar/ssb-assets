package com.secure_sentinel.accounts.accounts_service.service;

import org.hibernate.exception.LockAcquisitionException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class RetryUtility {
    public <T> T performWithRetry(Supplier<T> operation, int maxAttempts) {
        int attempts = 0;
        while (true) {
            try {
                return operation.get();
            } catch (PessimisticLockingFailureException | LockAcquisitionException | JpaSystemException ex) {
                if (++attempts >= maxAttempts) throw ex;
                applyExponentialBackoff(attempts);
            }
        }
    }

    public void performWithRetry(Runnable operation, int maxAttempts) {
        int attempts = 0;
        while (true) {
            try {
                operation.run();
                return;
            } catch (PessimisticLockingFailureException | LockAcquisitionException | JpaSystemException ex) {
                if (++attempts >= maxAttempts) throw ex;
                applyExponentialBackoff(attempts);
            }
        }
    }

    private void applyExponentialBackoff(int retryCount) {
        long baseTime = 200;
        double baseMultiplier = 1.5;
        long backoffTime = (long) (baseTime * Math.pow(baseMultiplier, retryCount));
        long MAX_BACKOFF_TIME = 5000;
        backoffTime = Math.min(backoffTime, MAX_BACKOFF_TIME);
        long jitter = (long) (Math.random() * 100);
        backoffTime += jitter;
        //approx:300,450,675...
        try {
            Thread.sleep(backoffTime);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}