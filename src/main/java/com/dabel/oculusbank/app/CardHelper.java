package com.dabel.oculusbank.app;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

public class CardHelper {

    public static boolean isValidMonth(String month) {
        int parsedMonth = Integer.parseInt(month);
        return parsedMonth >= 1 && parsedMonth <= 12;
    }

    public static boolean isValidYear(String year) {
        int parsedYear = Integer.parseInt(year);
        int actualYear = Year.now().getValue();
        List<Integer> years = IntStream.range(actualYear, actualYear + 10).boxed().toList();
        return years.contains(parsedYear);
    }

    public static LocalDate setExpirationDate(String month, String year) {
        String date = year + "/" + month + "/" + 1;
        LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/M/d"));
        return convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
    }

    public static String hideCardNumber(String cardNumber) {
        return new StringBuilder(cardNumber).replace(0, cardNumber.length() - 4, "****").toString();
    }
}
