package com.library.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    private DateUtil() {}
    public static LocalDate today() { return LocalDate.now(); }
    public static String format(LocalDate d) { return d.format(F); }
}
