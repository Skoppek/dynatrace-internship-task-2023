package com.skopmateusz.backendTask.errorHandlers;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class ErrorResponse {
    private HttpStatus status;
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
