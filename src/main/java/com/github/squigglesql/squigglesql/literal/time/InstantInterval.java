package com.github.squigglesql.squigglesql.literal.time;

import com.github.squigglesql.squigglesql.SquiggleConstants;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class InstantInterval extends AbstractTimeLiteral {

    public InstantInterval(TemporalAccessor value) {
        super(value);
    }

    @Override
    protected DateTimeFormatter getFormatter() {
        return SquiggleConstants.TIMESTAMP_WITH_TIME_ZONE_FORMATTER.withZone(ZoneOffset.UTC);
    }
}
