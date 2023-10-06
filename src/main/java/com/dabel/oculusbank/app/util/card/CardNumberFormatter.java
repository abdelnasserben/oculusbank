package com.dabel.oculusbank.app.util.card;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CardNumberFormatter {

    public static String hide(String cardNumber) {
        return new StringBuilder(cardNumber).replace(0, cardNumber.length() - 4, "****").toString();
    }
}
