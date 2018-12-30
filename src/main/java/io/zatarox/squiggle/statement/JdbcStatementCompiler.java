package io.zatarox.squiggle.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class JdbcStatementCompiler implements StatementCompiler<PreparedStatement> {

    private final Connection connection;

    public JdbcStatementCompiler(Connection connection) {
        this.connection = connection;
    }

    @Override
    public StatementBuilder<PreparedStatement> createStatementBuilder(String query) throws SQLException {
        return new Builder(connection.prepareStatement(query));
    }

    private static class Builder implements StatementBuilder<PreparedStatement> {

        private final PreparedStatement statement;

        private int lastIndex = 0;

        Builder(PreparedStatement statement) {
            this.statement = statement;
        }

        @Override
        public PreparedStatement buildStatement() {
            return statement;
        }

        @Override
        public void addBoolean(Boolean value) throws SQLException {
            if (value == null) {
                statement.setNull(++lastIndex, Types.BOOLEAN);
            } else {
                statement.setBoolean(++lastIndex, value);
            }
        }

        @Override
        public void addInteger(Integer value) throws SQLException {
            if (value == null) {
                statement.setNull(++lastIndex, Types.INTEGER);
            } else {
                statement.setInt(++lastIndex, value);
            }
        }

        @Override
        public void addString(String value) throws SQLException {
            // setNull is unnecessary https://stackoverflow.com/a/1357449/851159
            statement.setString(++lastIndex, value);
        }
    }
}
