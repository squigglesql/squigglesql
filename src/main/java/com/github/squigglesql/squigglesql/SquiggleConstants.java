/*
 * Copyright 2019 Egor Nepomnyaschih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.squigglesql.squigglesql;

import java.time.format.DateTimeFormatter;

/**
 * Squiggle SQL constants.
 */
public abstract class SquiggleConstants {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss.SSS";
    private static final String ZONE_PATTERN = "x";

    /**
     * Java 8 date/time API formatter to use for SQL date values.
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Java 8 date/time API formatter to use for SQL time values.
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    /**
     * Java 8 date/time API formatter to use for SQL timestamp values.
     */
    public static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN + " " + TIME_PATTERN);

    /**
     * Java 8 date/time API formatter to use for SQL timestamp with time zone values.
     */
    public static final DateTimeFormatter TIMESTAMP_WITH_TIME_ZONE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN + " " + TIME_PATTERN + ZONE_PATTERN);

    /**
     * Java 8 date/time API formatter to use for SQL time with time zone values.
     */
    public static final DateTimeFormatter TIME_WITH_TIME_ZONE_FORMATTER =
            DateTimeFormatter.ofPattern(TIME_PATTERN + ZONE_PATTERN);
}
