package com.intuit.urlshortner.exceptions;

public class ValidateRequestException extends RuntimeException{
    public ValidateRequestException(String message) {
        super(message);
    }

    public ValidateRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
