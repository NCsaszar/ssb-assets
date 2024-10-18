package com.smoothstack.branchservice.exception.custom;

import lombok.AllArgsConstructor;

/**
 * Generic exception for service type service issues.
 */
@AllArgsConstructor
public class ServiceTypeServiceException extends RuntimeException {
    public ServiceTypeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}