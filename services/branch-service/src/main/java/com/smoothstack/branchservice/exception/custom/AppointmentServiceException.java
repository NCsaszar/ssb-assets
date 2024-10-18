package com.smoothstack.branchservice.exception.custom;

import lombok.AllArgsConstructor;

/**
 * Generic exception for appointment service issues.
 */
@AllArgsConstructor
public class AppointmentServiceException extends RuntimeException {

    public AppointmentServiceException(String message) {
        super(message);
    }
    public AppointmentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}