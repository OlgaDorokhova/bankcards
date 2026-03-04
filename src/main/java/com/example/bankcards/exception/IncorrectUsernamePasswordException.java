package com.example.bankcards.exception;

public class IncorrectUsernamePasswordException extends RuntimeException {
    public IncorrectUsernamePasswordException(String message) {
        super(message);
    }
}
