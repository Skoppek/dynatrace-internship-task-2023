package com.skopmateusz.backendTask.errorHandlers;

import org.springframework.http.HttpStatus;

public class NbpException extends RuntimeException {
    private HttpStatus status;

    public NbpException(HttpStatus status,String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
