package io.zatarox.squiggle.literal.time;

import io.zatarox.squiggle.SquiggleConstants;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class TimeLiteral extends AbstractTimeLiteral {

    public TimeLiteral(TemporalAccessor value) {
        super(value);
    }

    @Override
    protected DateTimeFormatter getFormatter() {
        return SquiggleConstants.TIME_FORMATTER;
    }
}
