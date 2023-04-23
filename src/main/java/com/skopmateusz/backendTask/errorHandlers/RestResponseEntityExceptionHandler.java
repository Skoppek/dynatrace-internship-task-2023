package com.skopmateusz.backendTask.errorHandlers;

import com.skopmateusz.backendTask.exceptions.NbpNotFoundException;
import com.skopmateusz.backendTask.exceptions.NbpWrongParamsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            messages.add(violation.getMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST, messages
        );
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({NbpNotFoundException.class})
    public ResponseEntity<Object> handleNbpNotFoundException(NbpNotFoundException ex) {
        List<String> messages = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND, messages
        );
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({NbpWrongParamsException.class})
    public ResponseEntity<Object> handleNbpWrongParamsException(NbpWrongParamsException ex) {
        List<String> messages = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST, messages
        );
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
}
