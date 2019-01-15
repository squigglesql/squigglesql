package io.zatarox.squiggle.literal.time;

import io.zatarox.squiggle.SquiggleConstants;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateLiteral extends AbstractTimeLiteral {

    public DateLiteral(TemporalAccessor value) {
        super(value);
    }

    @Override
    protected DateTimeFormatter getFormatter() {
        return SquiggleConstants.DATE_FORMATTER;
    }
}
