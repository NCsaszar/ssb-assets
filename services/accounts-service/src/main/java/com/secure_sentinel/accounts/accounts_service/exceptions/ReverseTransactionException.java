package com.secure_sentinel.accounts.accounts_service.exceptions;

public class ReverseTransactionException extends RuntimeException {
    public ReverseTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}