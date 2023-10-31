package com.dabel.oculusbank.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenderValidator implements ConstraintValidator<Gender, String> {
    @Override
    public boolean isValid(String gender, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.dabel.oculusbank.constant.Gender.values())
                .anyMatch(g -> g.name().equals(gender));
    }
}
