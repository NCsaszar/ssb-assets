package com.secure_sentinel.accounts.accounts_service.exceptions;

public class TransferFundsException extends RuntimeException {
    public TransferFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}