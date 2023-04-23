package com.skopmateusz.backendTask.errorHandlers;

import com.skopmateusz.backendTask.exceptions.NbpNotFoundException;
import com.skopmateusz.backendTask.exceptions.NbpWrongParamsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            messages.add(violation.getMessage());
        }
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST, messages
        );
    }

    @ExceptionHandler({NbpNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleNbpNotFoundException(NbpNotFoundException ex) {
        List<String> messages = List.of(ex.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND, messages
        );
    }

    @ExceptionHandler({NbpWrongParamsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNbpWrongParamsException(NbpWrongParamsException ex) {
        List<String> messages = List.of(ex.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST, messages
        );
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatusCode status,
                                                                   WebRequest request) {
        List<String> messages = List.of(String.format("Status %s: Something went wrong...", status.toString()));
        ErrorResponse errorResponse = new ErrorResponse(
                (HttpStatus) status, messages
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
