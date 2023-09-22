package com.dabel.oculusbank.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CurrencyValidator implements ConstraintValidator<Currency, String> {
    @Override
    public boolean isValid(String currency, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.dabel.oculusbank.constant.Currency.values())
                .anyMatch(c -> c.name().equals(currency));
    }
}
