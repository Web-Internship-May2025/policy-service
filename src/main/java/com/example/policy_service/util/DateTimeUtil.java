package com.example.policy_service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static final String DATE_FORMAT_PATTERN = "dd-MM-yyyy HH:mm";

    // regex pattern that matches "dd-MM-yyyy HH:mm"
    public static final String DATE_FORMAT_REGEX = "^\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}$";

    private static final DateTimeFormatter ISO_INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

    public static String convertToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(OUTPUT_FORMATTER);
    }

    public static LocalDateTime parseIsoString(String isoString) {
        if (isoString == null || isoString.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(isoString, ISO_INPUT_FORMATTER);
    }
}
