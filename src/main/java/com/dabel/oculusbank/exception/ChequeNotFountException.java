package com.dabel.oculusbank.exception;

public class ChequeNotFountException extends RuntimeException {
    public ChequeNotFountException() {
        super("Cheque not found");
    }
}
