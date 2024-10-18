package com.SecureSentinel.CardsLoans.Exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CardControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ApiError> handleBadRequestException(RuntimeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(status, ex.getMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Access Denied");
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Authentication Failed");
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }
}