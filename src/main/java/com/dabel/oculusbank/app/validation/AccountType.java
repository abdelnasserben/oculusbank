package com.dabel.oculusbank.app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= AccountTypeValidator.class)
public @interface AccountType {
    String message() default "account type must be Saving or Business";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
