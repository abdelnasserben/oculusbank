package com.dabel.oculusbank.exception;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException() {
        super("Branch not found");
    }
}
