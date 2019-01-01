package io.zatarox.squiggle.statement;

import java.sql.SQLException;
import java.sql.Timestamp;

public interface Parametrized {

    void addBoolean(Boolean value) throws SQLException;

    void addInteger(Integer value) throws SQLException;

    void addString(String value) throws SQLException;

    void addTimestamp(Timestamp value) throws SQLException;
}
