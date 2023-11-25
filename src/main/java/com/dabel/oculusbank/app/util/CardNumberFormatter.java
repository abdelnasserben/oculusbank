package com.dabel.oculusbank.app.util;

public class CardNumberFormatter {

    public static String hide(String cardNumber) {
        return new StringBuilder(cardNumber).replace(0, cardNumber.length() - 4, "****").toString();
    }
}
