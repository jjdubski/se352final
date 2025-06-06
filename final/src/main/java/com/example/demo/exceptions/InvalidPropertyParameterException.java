package com.example.demo.exceptions;

public class InvalidPropertyParameterException extends RuntimeException {
    public InvalidPropertyParameterException(String message) {
        super(message);
    }
}
