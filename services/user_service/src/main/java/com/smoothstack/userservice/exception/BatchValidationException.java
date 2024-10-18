package com.smoothstack.userservice.exception;

import java.util.List;

public class BatchValidationException extends RuntimeException {
    private final List<String> validationErrors;

    public BatchValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
