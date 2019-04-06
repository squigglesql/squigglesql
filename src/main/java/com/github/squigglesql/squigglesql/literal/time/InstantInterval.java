package com.github.squigglesql.squigglesql.literal.time;

import com.github.squigglesql.squigglesql.SquiggleConstants;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Date/time literal. Use {@link com.github.squigglesql.squigglesql.literal.Literal#of(java.time.Instant)} method to
 * instantiate it.
 */
public class InstantInterval extends AbstractTimeLiteral {

    /**
     * Creates a literal.
     * @param value literal value.
     */
    public InstantInterval(Instant value) {
        super(value);
    }

    @Override
    protected DateTimeFormatter getFormatter() {
        return SquiggleConstants.TIMESTAMP_WITH_TIME_ZONE_FORMATTER.withZone(ZoneOffset.UTC);
    }
}
