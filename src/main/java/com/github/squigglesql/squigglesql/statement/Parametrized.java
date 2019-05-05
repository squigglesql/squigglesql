/*
 * Copyright 2019 Egor Nepomnyaschih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
     * Delegate of {@link java.sql.PreparedStatement#setBoolean(int, boolean)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addBoolean(boolean value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setByte(int, byte)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addByte(byte value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setShort(int, short)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addShort(short value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setInt(int, int)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addInteger(int value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setLong(int, long)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addLong(long value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setFloat(int, float)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addFloat(float value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setDouble(int, double)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addDouble(double value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setBigDecimal(int, BigDecimal)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addBigDecimal(BigDecimal value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setString(int, String)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addString(String value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setTimestamp(int, Timestamp, Calendar)} method.
     *
     * @param value    JDBC statement method argument.
     * @param calendar JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addTimestamp(Timestamp value, Calendar calendar) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setTime(int, Time, Calendar)} method.
     *
     * @param value    JDBC statement method argument.
     * @param calendar JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addTime(Time value, Calendar calendar) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setDate(int, Date, Calendar)} method.
     *
     * @param value    JDBC statement method argument.
     * @param calendar JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addDate(Date value, Calendar calendar) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setArray(int, Array)} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addArray(Array value) throws SQLException;

    /**
     * Delegate of {@link java.sql.PreparedStatement#setBytes(int, byte[])} method.
     *
     * @param value JDBC statement method argument.
     * @throws SQLException if JDBC throws the exception.
     */
    void addBytes(byte[] value) throws SQLException;
}
