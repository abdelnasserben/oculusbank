package com.dabel.oculusbank.app.util;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.IntStream;

public class CardExpirationDateSettlement {

    public static boolean isValidMonth(String month) {
        int parsedMonth = Integer.parseInt(month);
        return parsedMonth >= 1 && parsedMonth <= 12;
    }

    public static boolean isValidYear(String year) {
        int parsedYear = Integer.parseInt(year);
        int actualYear = Year.now().getValue();
        return IntStream.range(actualYear, actualYear + 10).anyMatch(y -> y == parsedYear);
    }

    public static LocalDate setDate(String month, String year) {
        String date = year + "/" + month + "/" + 1;
        LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/M/d", Locale.US));
        return convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
    }

}
