package com.intuit.urlshortner.controller;

import com.intuit.urlshortner.dto.response.ErrorResponse;
import com.intuit.urlshortner.exceptions.CustomShortUrlConflictException;
import com.intuit.urlshortner.exceptions.ShortUrlNotFoundException;
import com.intuit.urlshortner.exceptions.ValidateRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomShortUrlConflictException.class)
    public ResponseEntity<ErrorResponse> handleCustomShortUrlConflictException(CustomShortUrlConflictException ex) {
        ErrorResponse errorResponse = new ErrorResponse().setErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShortUrlNotFoundException(ShortUrlNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse().setErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ValidateRequestException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidateRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse().setErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}

