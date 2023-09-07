package com.dabel.oculusbank.exception;

public class IllegalTransactionException extends RuntimeException {

    public IllegalTransactionException(String message) {
        super(message);
    }
}
