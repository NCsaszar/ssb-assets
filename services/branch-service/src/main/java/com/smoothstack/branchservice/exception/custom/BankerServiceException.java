package com.smoothstack.branchservice.exception.custom;

import lombok.AllArgsConstructor;

/**
 * Generic exception for branch service issues.
 */
@AllArgsConstructor
public class BankerServiceException extends RuntimeException {
    public BankerServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}