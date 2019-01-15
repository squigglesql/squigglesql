package io.zatarox.squiggle.literal.time;

import io.zatarox.squiggle.SquiggleConstants;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class TimestampWithTimeZoneLiteral extends AbstractTimeLiteral {

    public TimestampWithTimeZoneLiteral(TemporalAccessor value) {
        super(value);
    }

    @Override
    protected DateTimeFormatter getFormatter() {
        return SquiggleConstants.TIMESTAMP_WITH_TIME_ZONE_FORMATTER;
    }
}
