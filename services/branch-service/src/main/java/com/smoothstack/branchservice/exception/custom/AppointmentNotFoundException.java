package com.smoothstack.branchservice.exception.custom;

import lombok.AllArgsConstructor;

/**
 * Exception for appointment not found issues.
 */

@AllArgsConstructor
public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
