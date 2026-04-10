package com.stockmanager.util;

import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String format(java.time.LocalDateTime date) {
        if (date == null) return "";
        return date.format(FORMATTER);
    }
}
