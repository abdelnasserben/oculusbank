package com.dabel.oculusbank.app.custom.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class TransactionTypeValidator implements ConstraintValidator<TransactionType, String> {

    @Override
    public boolean isValid(String transactionType, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.dabel.oculusbank.constant.TransactionType.values())
                .anyMatch(t -> t.name().equals(transactionType));
    }
}
