package com.intuit.urlshortner.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomUrlValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCustomUrl {
    String message() default "Invalid custom URL";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
