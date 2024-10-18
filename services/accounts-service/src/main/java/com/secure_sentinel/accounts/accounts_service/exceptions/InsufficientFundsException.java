package com.secure_sentinel.accounts.accounts_service.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class InsufficientFundsException extends RuntimeException {
    private final Map<String, Object> errorDetails;

    public InsufficientFundsException(String message, Map<String, Object> errorDetails) {
        super(message);
        this.errorDetails = errorDetails;
    }

    public InsufficientFundsException(String insufficientFunds) {
        this(insufficientFunds, null);
    }
}