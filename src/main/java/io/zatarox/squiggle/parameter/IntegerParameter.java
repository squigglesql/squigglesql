package io.zatarox.squiggle.parameter;

import io.zatarox.squiggle.statement.Parametrized;

import java.sql.SQLException;

public class IntegerParameter extends Parameter {

    private final Integer value;

    public IntegerParameter(Integer value) {
        this.value = value;
    }

    @Override
    public void addValue(Parametrized builder) throws SQLException {
        builder.addInteger(value);
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
