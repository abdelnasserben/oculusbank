package com.dabel.oculusbank.app.custom.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CountryValidator implements ConstraintValidator<Country, String> {

    @Override
    public boolean isValid(String country, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.dabel.oculusbank.constant.Country.values())
                .anyMatch(t -> t.getName().equals(country));
    }
}
