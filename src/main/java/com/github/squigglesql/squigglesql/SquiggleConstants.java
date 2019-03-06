package com.github.squigglesql.squigglesql;

import java.time.format.DateTimeFormatter;

public abstract class SquiggleConstants {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss.SSS";
    private static final String ZONE_PATTERN = "x";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    public static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN + " " + TIME_PATTERN);
    public static final DateTimeFormatter TIMESTAMP_WITH_TIME_ZONE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN + " " + TIME_PATTERN + ZONE_PATTERN);
    public static final DateTimeFormatter TIME_WITH_TIME_ZONE_FORMATTER =
            DateTimeFormatter.ofPattern(TIME_PATTERN + ZONE_PATTERN);
}
