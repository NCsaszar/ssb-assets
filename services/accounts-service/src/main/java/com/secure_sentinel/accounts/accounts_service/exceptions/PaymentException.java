package com.secure_sentinel.accounts.accounts_service.exceptions;

public class PaymentException extends RuntimeException {
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}