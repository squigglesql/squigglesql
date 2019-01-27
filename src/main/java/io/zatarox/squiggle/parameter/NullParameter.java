package io.zatarox.squiggle.parameter;

import io.zatarox.squiggle.statement.Parametrized;

import java.sql.SQLException;
import java.sql.Types;

class NullParameter extends Parameter {

    static final Parameter BOOLEAN = new NullParameter(Types.BOOLEAN);
    static final Parameter BYTE = new NullParameter(Types.TINYINT);
    static final Parameter SHORT = new NullParameter(Types.SMALLINT);
    static final Parameter INTEGER = new NullParameter(Types.INTEGER);
    static final Parameter LONG = new NullParameter(Types.BIGINT);
    static final Parameter FLOAT = new NullParameter(Types.REAL);
    static final Parameter DOUBLE = new NullParameter(Types.DOUBLE);

    private final int sqlType;

    NullParameter(int sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public void addValue(Parametrized builder) throws SQLException {
        builder.addNull(sqlType);
    }
}
