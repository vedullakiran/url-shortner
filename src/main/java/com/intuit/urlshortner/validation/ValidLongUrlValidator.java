package com.intuit.urlshortner.validation;

import com.intuit.urlshortner.exceptions.ValidateRequestException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.URL;

public class ValidLongUrlValidator implements ConstraintValidator<ValidUrl, String> {

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        if (url == null) {
            throw new ValidateRequestException("Url can not be null");
        }

        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            throw new ValidateRequestException("Not a valid url");
        }
    }
}

