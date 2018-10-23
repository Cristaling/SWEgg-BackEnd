package io.github.cristaling.swegg.backend.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static LocalDate parseLocalDateFromString(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }

}
