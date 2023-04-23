package com.skopmateusz.backendTask.errorHandlers;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class ErrorResponse {
    private HttpStatus status;
    private List<String> messages;

    public ErrorResponse(HttpStatus status, List<String> messages) {
        super();
        this.status = status;
        this.messages = messages;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getMessage() {
        return messages;
    }
}
