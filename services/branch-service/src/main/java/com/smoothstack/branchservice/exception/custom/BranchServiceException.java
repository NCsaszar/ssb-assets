package com.smoothstack.branchservice.exception.custom;

import lombok.AllArgsConstructor;

/**
 * Generic exception for branch service issues.
 */
@AllArgsConstructor
public class BranchServiceException extends RuntimeException {
    public BranchServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}