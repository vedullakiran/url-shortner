package com.intuit.urlshortner.validation;

import com.intuit.urlshortner.exceptions.ValidateRequestException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CustomUrlValidator implements ConstraintValidator<ValidCustomUrl, String> {

    private static final String VALIDATION_PATTERN = "^[a-zA-Z0-9_$-_.+!*’(),]*$"; // Add allowed special characters if needed

    @Override
    public boolean isValid(String customUrl, ConstraintValidatorContext context) {
        if (customUrl == null) {
            throw new ValidateRequestException("Custom url cannot be null"); //  null values not allowed
        }
        int length = customUrl.trim().length();
        if (length < 8 || length > 16) {
            throw new ValidateRequestException("Custom url should contain at MAX 16 characters");
        }

        if (!Pattern.matches(VALIDATION_PATTERN, customUrl)) {
            throw new ValidateRequestException(String.format("Custom url should follow the following pattern %s", VALIDATION_PATTERN));
        }

        return Pattern.matches(VALIDATION_PATTERN, customUrl);
    }
}
