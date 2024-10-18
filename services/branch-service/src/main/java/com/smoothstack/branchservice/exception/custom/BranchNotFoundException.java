package com.smoothstack.branchservice.exception.custom;

import lombok.AllArgsConstructor;

/**
 * Exception for branch not found issues.
 */
@AllArgsConstructor
public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BranchNotFoundException(String message) {
        super(message);
    }
}