package com.example.demo.exceptions;

public class InvalidMessageParameterException extends RuntimeException {
    public InvalidMessageParameterException(String message) {
        super(message);
    }
}
