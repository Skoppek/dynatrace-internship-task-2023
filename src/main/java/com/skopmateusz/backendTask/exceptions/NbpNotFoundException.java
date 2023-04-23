package com.skopmateusz.backendTask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NbpNotFoundException extends RuntimeException {
    private HttpStatus status;

    public NbpNotFoundException(String message) {
        super(message);
    }
}
