package com.dabel.oculusbank.app;

import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.ExchangeCourse;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CurrencyExchanger {

    public static double exchange(String receivedCurrency, String givenCurrency, double amount) {

        if(isKmf2Eur(receivedCurrency, givenCurrency))
            amount /= ExchangeCourse.SALE_EUR;

        if(isEur2Kmf(receivedCurrency, givenCurrency))
            amount *= ExchangeCourse.BUY_EUR;

        if(isKmf2Usd(receivedCurrency, givenCurrency))
            amount /= ExchangeCourse.SALE_USD;

        if(isUsd2Kmf(receivedCurrency, givenCurrency))
            amount *= ExchangeCourse.BUY_USD;

        DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);

        return Double.parseDouble(decimalFormat.format(amount));
    }

    private static boolean isKmf2Eur(String currency1, String currency2) {
        return currency1.equals(Currency.KMF.name()) && currency2.equals(Currency.EUR.name());
    }

    private static boolean isEur2Kmf(String currency1, String currency2) {
        return currency1.equals(Currency.EUR.name()) && currency2.equals(Currency.KMF.name());
    }

    private static boolean isKmf2Usd(String currency1, String currency2) {
        return currency1.equals(Currency.KMF.name()) && currency2.equals(Currency.USD.name());
    }

    private static boolean isUsd2Kmf(String currency1, String currency2) {
        return currency1.equals(Currency.USD.name()) && currency2.equals(Currency.KMF.name());
    }

}
