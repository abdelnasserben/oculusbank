package com.dabel.oculusbank.exception;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException() {
        super("Card not found");
    }

}
