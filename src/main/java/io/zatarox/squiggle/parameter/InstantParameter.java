package io.zatarox.squiggle.parameter;

import io.zatarox.squiggle.statement.Parametrized;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class InstantParameter extends Parameter {

    private final Instant value;

    public InstantParameter(Instant value) {
        this.value = value;
    }

    @Override
    public void addValue(Parametrized builder) throws SQLException {
        builder.addTimestamp(Timestamp.from(value));
    }
}
