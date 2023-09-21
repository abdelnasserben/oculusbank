package com.dabel.oculusbank.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CardTypeValidator implements ConstraintValidator<CardType, String> {
    @Override
    public boolean isValid(String cardType, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.dabel.oculusbank.constant.CardType.values())
                .map(Enum::name)
                .toList()
                .contains(cardType);
    }
}
