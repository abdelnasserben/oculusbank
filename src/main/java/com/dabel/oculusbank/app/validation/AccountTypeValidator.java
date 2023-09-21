package com.dabel.oculusbank.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AccountTypeValidator implements ConstraintValidator<AccountType, String> {
    @Override
    public boolean isValid(String accountType, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.dabel.oculusbank.constant.AccountType.values())
                .map(Enum::name)
                .toList()
                .contains(accountType);
    }
}
