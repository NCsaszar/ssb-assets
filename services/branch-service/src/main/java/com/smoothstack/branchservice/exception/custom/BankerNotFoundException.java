package com.smoothstack.branchservice.exception.custom;

import lombok.AllArgsConstructor;

/**
 * Exception for appointment not found issues.
 */

@AllArgsConstructor
public class BankerNotFoundException extends RuntimeException {
    public BankerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
