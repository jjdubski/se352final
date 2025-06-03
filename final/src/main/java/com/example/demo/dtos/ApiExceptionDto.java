package com.example.demo.dtos;

import java.time.LocalDateTime;

public class ApiExceptionDto {
    private String error;
    private int status;
    private LocalDateTime timestamp;
    private String path;
    private String exception;

    public ApiExceptionDto() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiExceptionDto(String error, int status, String path, String exception) {
        this.error = error;
        this.status = status;
        this.path = path;
        this.exception = exception;
        this.timestamp = LocalDateTime.now();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

}

