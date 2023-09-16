package com.dabel.oculusbank.exception;

public class CardAppRequestNotFoundException extends RuntimeException {

    public CardAppRequestNotFoundException() {
        super("Card application request not found");
    }

}
