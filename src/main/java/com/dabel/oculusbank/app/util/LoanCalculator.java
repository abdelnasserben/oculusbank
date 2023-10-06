package com.dabel.oculusbank.app.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class LoanCalculator {

    private final DecimalFormat decimalFormat = new DecimalFormat("##.##", new DecimalFormatSymbols(Locale.US));
    private final double issuedAmount, interestRate;
    private final int duration;

    public LoanCalculator(double issuedAmount, double interestRate, int duration) {
        this.issuedAmount = issuedAmount;
        this.interestRate = interestRate;
        this.duration = duration;
    }

    public double getTotalAmountDue() {
        double totalAmount = (issuedAmount * interestRate / 100) + issuedAmount;
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);

        return Double.parseDouble(decimalFormat.format(totalAmount));
    }

    public double getPerMonthAmountDue() {
        double perMonthAmount = getTotalAmountDue() / duration;
        decimalFormat.setRoundingMode(RoundingMode.UP);

        return Double.parseDouble(decimalFormat.format(perMonthAmount));
    }
}
