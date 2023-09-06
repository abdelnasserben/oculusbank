package com.dabel.oculusbank.exception;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException() {
        super("Loan not found");
    }
}
