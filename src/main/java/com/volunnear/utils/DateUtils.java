package com.volunnear.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Date convertStringToDate(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, FORMATTER);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
