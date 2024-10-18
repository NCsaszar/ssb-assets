package com.SecureSentinel.CardsLoans.Exceptions;

public class UserCardException extends RuntimeException {

    public UserCardException(String message) {
        super(message);
    }

    public UserCardException(String message, Throwable cause) {
        super(message, cause);
    }
}

