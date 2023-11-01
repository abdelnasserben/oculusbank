package com.dabel.oculusbank.app.custom.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CardTypeValidator implements ConstraintValidator<CardType, String> {
    @Override
    public boolean isValid(String cardType, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.dabel.oculusbank.constant.CardType.values())
                .anyMatch(c -> c.name().equals(cardType));
    }
}
