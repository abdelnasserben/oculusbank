package com.dabel.oculusbank.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException() {
        super("Transaction not found");
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
