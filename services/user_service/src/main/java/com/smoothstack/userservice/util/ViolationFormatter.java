package com.smoothstack.userservice.util;

import com.smoothstack.userservice.model.AppUser;
import jakarta.validation.ConstraintViolation;

public class ViolationFormatter {

    public static String formatConstraintViolation(AppUser item, ConstraintViolation<?> violation) {
        return String.format(
                "Username: %s, Email: %s, Error: %s: %s",
                item != null ? item.getUsername() : "N/A",
                item != null ? item.getEmail() : "N/A",
                violation.getPropertyPath(),
                violation.getMessage()
        );
    }

    public static String formatConstraintViolation(String username, String email, ConstraintViolation<?> violation) {
        return String.format(
                "Username: %s, Email: %s, Error: %s: %s",
                username != null ? username : "N/A",
                email != null ? email : "N/A",
                violation.getPropertyPath(),
                violation.getMessage()
        );
    }
}
