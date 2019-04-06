package com.github.squigglesql.squigglesql.statement;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Squiggle SQL adapter over {@link java.sql.PreparedStatement} allowing mock object implementation.
 */
public interface Parametrized {

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     *
     * @param sqlType SQL type.
     * @throws SQLException if JDBC throws the exception.
     */
    void addNull(int sqlType) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addBoolean(boolean value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addByte(byte value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addShort(short value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addInteger(int value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addLong(long value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addFloat(float value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addDouble(double value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addBigDecimal(BigDecimal value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addString(String value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @param calendar JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addTimestamp(Timestamp value, Calendar calendar) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @param calendar JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addTime(Time value, Calendar calendar) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @param calendar JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addDate(Date value, Calendar calendar) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addArray(Array value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setNull(int, int)} method.
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addBytes(byte[] value) throws SQLException;
}
