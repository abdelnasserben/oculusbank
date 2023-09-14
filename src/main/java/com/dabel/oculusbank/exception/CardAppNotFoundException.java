package com.dabel.oculusbank.exception;

public class CardAppNotFoundException extends RuntimeException {

    public CardAppNotFoundException() {
        super("Card application request not found");
    }

}
