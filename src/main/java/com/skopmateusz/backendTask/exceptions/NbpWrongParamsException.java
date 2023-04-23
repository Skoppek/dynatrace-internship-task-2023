package com.skopmateusz.backendTask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NbpWrongParamsException extends RuntimeException {
    private HttpStatus status;

    public NbpWrongParamsException(String message) {
        super(message);
    }
}
