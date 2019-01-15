package io.zatarox.squiggle.literal.time;

import io.zatarox.squiggle.literal.AbstractStringLiteral;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public abstract class AbstractTimeLiteral extends AbstractStringLiteral {

    private final TemporalAccessor value;

    public AbstractTimeLiteral(TemporalAccessor value) {
        this.value = value;
    }

    @Override
    protected Object getValue() {
        return getFormatter().format(value);
    }

    protected abstract DateTimeFormatter getFormatter();
}
