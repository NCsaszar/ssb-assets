package com.secure_sentinel.accounts.accounts_service.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class ApiError {
    private HttpStatus status;
    private String message;
    private Map<String, Object> errorDetails;

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, Map<String, Object> errorDetails) {
        this.status = status;
        this.message = message;
        this.errorDetails = errorDetails;
    }
}