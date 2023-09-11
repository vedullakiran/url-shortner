package com.intuit.urlshortner.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CustomShortUrlConflictException extends RuntimeException {

    public CustomShortUrlConflictException(String customShortUrl) {
        super("Custom short URL '" + customShortUrl + "' already taken");
    }

    public CustomShortUrlConflictException(Throwable e) {
        super(e);
    }

    public CustomShortUrlConflictException(String s, DataIntegrityViolationException ex) {
        super(s, ex);
    }
}

