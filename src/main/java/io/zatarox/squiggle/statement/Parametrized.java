package io.zatarox.squiggle.statement;

import java.sql.SQLException;

public interface Parametrized {

    void addBoolean(Boolean value) throws SQLException;

    void addInteger(Integer value) throws SQLException;

    void addString(String value) throws SQLException;
}
