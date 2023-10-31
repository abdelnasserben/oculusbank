package com.dabel.oculusbank.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class IdentityTypeValidator implements ConstraintValidator<IdentityType, String> {
    @Override
    public boolean isValid(String identityType, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.dabel.oculusbank.constant.IdentityType.values())
                .anyMatch(i -> i.name().equals(identityType));
    }
}
