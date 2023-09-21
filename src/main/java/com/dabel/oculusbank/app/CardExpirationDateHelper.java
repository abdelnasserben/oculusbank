package com.dabel.oculusbank.app;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

public class CardExpirationDateHelper {

    public static boolean isMonth(String month) {
        int parsedMonth = Integer.parseInt(month);
        return parsedMonth >= 1 && parsedMonth <= 12;
    }

    public static boolean isYear(String year) {
        int parsedYear = Integer.parseInt(year);
        int actualYear = Year.now().getValue();
        List<Integer> years = IntStream.range(actualYear, actualYear + 10).boxed().toList();
        return years.contains(parsedYear);
    }

    public static LocalDate setDate(String month, String year) {
        String date = year + "/" + month + "/" + 1;
        LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/M/d"));
        return convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
    }
}
