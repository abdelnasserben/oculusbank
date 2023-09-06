package com.dabel.oculusbank.exception;

public class BalanceInsufficientException extends RuntimeException {
    public BalanceInsufficientException() {
        super("Account balance is insufficient");
    }
}
