package com.dabel.oculusbank.exception;

public class IllegalTransactionException extends RuntimeException {
    public IllegalTransactionException() {
        super("Illegal transaction");
    }

    public IllegalTransactionException(String message) {
        super(message);
    }
}
