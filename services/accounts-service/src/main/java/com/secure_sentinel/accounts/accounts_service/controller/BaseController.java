package com.secure_sentinel.accounts.accounts_service.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    @AllArgsConstructor
    @Getter
    public static class CustomResponse<T> {
        private final HttpStatus status;
        private final T data;

        public CustomResponse(HttpStatus status) {
            this.status = status;
            this.data = null;
        }
    }

    protected <T> ResponseEntity<CustomResponse<T>> respondWith(T data, HttpStatus status) {
        CustomResponse<T> response = new CustomResponse<>(status, data);
        return new ResponseEntity<>(response, status);
    }

    protected ResponseEntity<?> respondWithNoContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}