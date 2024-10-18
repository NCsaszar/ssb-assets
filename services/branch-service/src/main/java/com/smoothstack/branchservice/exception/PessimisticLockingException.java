package com.smoothstack.branchservice.exception;

import lombok.AllArgsConstructor;

/**
 * Exception for being locked out of updating process.
 */
@AllArgsConstructor
public class PessimisticLockingException extends RuntimeException {
    public PessimisticLockingException(String message, Throwable cause) {
        super(message, cause);
    }
}