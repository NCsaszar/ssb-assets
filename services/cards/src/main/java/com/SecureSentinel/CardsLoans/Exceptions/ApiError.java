package com.SecureSentinel.CardsLoans.Exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class ApiError {
    private HttpStatus status;
    private String message;


    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}