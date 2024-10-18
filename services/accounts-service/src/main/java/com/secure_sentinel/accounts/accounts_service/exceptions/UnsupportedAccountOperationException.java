package com.secure_sentinel.accounts.accounts_service.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class UnsupportedAccountOperationException extends RuntimeException {
    private final Map<String, Object> errorDetails;

    public UnsupportedAccountOperationException(String message, Map<String, Object> errorDetails) {
        super(message);
        this.errorDetails = errorDetails;
    }

    public UnsupportedAccountOperationException(String unsupportedOperation) {
        super(unsupportedOperation);
        this.errorDetails = null;
    }
}