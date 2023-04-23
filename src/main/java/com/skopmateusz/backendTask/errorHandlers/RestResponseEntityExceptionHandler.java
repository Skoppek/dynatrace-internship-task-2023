package com.skopmateusz.backendTask.errorHandlers;

import com.skopmateusz.backendTask.exceptions.NbpNotFoundException;
import com.skopmateusz.backendTask.exceptions.NbpWrongParamsException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()
        );
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({NbpNotFoundException.class})
    public ResponseEntity<Object> handleNbpNotFoundException(NbpNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage()
        );
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({NbpWrongParamsException.class})
    public ResponseEntity<Object> handleNbpWrongParamsException(NbpWrongParamsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage()
        );
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
}
