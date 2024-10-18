package com.smoothstack.branchservice.exception.custom;

import lombok.AllArgsConstructor;

/**
 * Exception for service type not found issues.
 */
@AllArgsConstructor
public class ServiceTypeNotFoundException extends RuntimeException {
    public ServiceTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}