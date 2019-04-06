package com.github.squigglesql.squigglesql.statement;

import com.github.squigglesql.squigglesql.syntax.AbstractSqlSyntax;
import com.github.squigglesql.squigglesql.syntax.SqlSyntax;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Concrete {@link StatementCompiler} implementation for JDBC {@link PreparedStatement}.
 */
public class JdbcStatementCompiler implements StatementCompiler<PreparedStatement> {

    private final Connection connection;

    /**
     * Creates a JDBC statement compiler.
     *
     * @param connection JDBC connection to create a statement with.
     */
    public JdbcStatementCompiler(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AbstractSqlSyntax detectDefaultSyntax() throws SQLException {
        return SqlSyntax.from(connection);
    }

    @Override
    public StatementBuilder<PreparedStatement> createStatementBuilder(String query) throws SQLException {
        return new Builder(connection.prepareStatement(query));
    }

    @Override
    public StatementBuilder<PreparedStatement> createInsertStatementBuilder(String query) throws SQLException {
        return new Builder(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS));
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
        public void addNull(int sqlType) throws SQLException {
            statement.setNull(++lastIndex, sqlType);
        }

        @Override
        public void addBoolean(boolean value) throws SQLException {
            statement.setBoolean(++lastIndex, value);
        }

        @Override
        public void addByte(byte value) throws SQLException {
            statement.setByte(++lastIndex, value);
        }

        @Override
        public void addShort(short value) throws SQLException {
            statement.setShort(++lastIndex, value);
        }

        @Override
        public void addInteger(int value) throws SQLException {
            statement.setInt(++lastIndex, value);
        }

        @Override
        public void addLong(long value) throws SQLException {
            statement.setLong(++lastIndex, value);
        }

        @Override
        public void addFloat(float value) throws SQLException {
            statement.setFloat(++lastIndex, value);
        }

        @Override
        public void addDouble(double value) throws SQLException {
            statement.setDouble(++lastIndex, value);
        }

        // setNull for the following methods is unnecessary https://stackoverflow.com/a/1357449/851159
        @Override
        public void addBigDecimal(BigDecimal value) throws SQLException {
            statement.setBigDecimal(++lastIndex, value);
        }

        @Override
        public void addString(String value) throws SQLException {
            statement.setString(++lastIndex, value);
        }

        @Override
        public void addTimestamp(Timestamp value, Calendar calendar) throws SQLException {
            statement.setTimestamp(++lastIndex, value, calendar);
        }

        @Override
        public void addTime(Time value, Calendar calendar) throws SQLException {
            statement.setTime(++lastIndex, value, calendar);
        }

        @Override
        public void addDate(Date value, Calendar calendar) throws SQLException {
            statement.setDate(++lastIndex, value, calendar);
        }

        @Override
        public void addArray(Array value) throws SQLException {
            statement.setArray(++lastIndex, value);
        }

        @Override
        public void addBytes(byte[] value) throws SQLException {
            statement.setBytes(++lastIndex, value);
        }
    }
}
