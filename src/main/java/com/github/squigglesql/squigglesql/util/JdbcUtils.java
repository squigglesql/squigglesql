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
package com.github.squigglesql.squigglesql.util;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.query.Query;
import com.github.squigglesql.squigglesql.statement.JdbcStatementCompiler;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Utilities for {@link ResultSet} parsing.
 */
public abstract class JdbcUtils {

    /**
     * Reads a boolean value from {@link ResultSet}. The corresponding SQL type is BOOLEAN with NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(boolean)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(boolean)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static boolean readBooleanNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getBoolean(index);
    }

    /**
     * Reads a boolean value from {@link ResultSet}. The corresponding SQL type is BOOLEAN with NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Boolean)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(Boolean)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Boolean readBooleanNull(ResultSet rs, int index) throws SQLException {
        return (Boolean) rs.getObject(index);
    }

    /**
     * Reads a byte value from {@link ResultSet}. The corresponding SQL type is SMALLINT with NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(byte)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(byte)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static byte readByteNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getByte(index);
    }

    /**
     * Reads a byte value from {@link ResultSet}. The corresponding SQL type is SMALLINT with NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Byte)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(Byte)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Byte readByteNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Byte) ? (Byte) obj : Byte.valueOf(rs.getByte(index));
    }

    /**
     * Reads a short integer value from {@link ResultSet}. The corresponding SQL type is SMALLINT with NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(short)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(short)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static short readShortNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getShort(index);
    }

    /**
     * Reads a short integer value from {@link ResultSet}. The corresponding SQL type is SMALLINT with NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Short)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(Short)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Short readShortNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Short) ? (Short) obj : Short.valueOf(rs.getShort(index));
    }

    /**
     * Reads an integer value from {@link ResultSet}. The corresponding SQL type is INT with NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(int)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(int)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static int readIntegerNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getInt(index);
    }

    /**
     * Reads an integer value from {@link ResultSet}. The corresponding SQL type is INT with NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Integer)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(Integer)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Integer readIntegerNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Integer) ? (Integer) obj : Integer.valueOf(rs.getInt(index));
    }

    /**
     * Reads a long integer value from {@link ResultSet}. The corresponding SQL type is BIGINT with NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(long)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(long)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static long readLongNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getLong(index);
    }

    /**
     * Reads a long integer value from {@link ResultSet}. The corresponding SQL type is BIGINT with NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Long)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(Long)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Long readLongNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Long) ? (Long) obj : Long.valueOf(rs.getLong(index));
    }

    /**
     * Reads a floating point number value from {@link ResultSet}. The corresponding SQL type is REAL with NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(float)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(float)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static float readFloatNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getFloat(index);
    }

    /**
     * Reads a floating point number value from {@link ResultSet}. The corresponding SQL type is REAL with NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Float)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(Float)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Float readFloatNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Float) ? (Float) obj : Float.valueOf(rs.getFloat(index));
    }

    /**
     * Reads a double precision floating point number value from {@link ResultSet}.
     * The corresponding SQL type is DOUBLE PRECISION with NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(double)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(double)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static double readDoubleNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getDouble(index);
    }

    /**
     * Reads a double precision floating point number value from {@link ResultSet}.
     * The corresponding SQL type is DOUBLE PRECISION with NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Double)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(Double)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Double readDoubleNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Double) ? (Double) obj : Double.valueOf(rs.getDouble(index));
    }

    /**
     * Reads a big decimal value from {@link ResultSet}. The corresponding SQL type is NUMERIC with a preferred
     * precision, regardless of NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(BigDecimal)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(BigDecimal)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static BigDecimal readBigDecimal(ResultSet rs, int index) throws SQLException {
        return rs.getBigDecimal(index);
    }

    /**
     * Reads a string value from {@link ResultSet}. The corresponding SQL type is TEXT, regardless of NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(String)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(Object)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static String readString(ResultSet rs, int index) throws SQLException {
        return rs.getString(index);
    }

    /**
     * Reads a timestamp value from {@link ResultSet}. The corresponding SQL type is TIMESTAMP with a preferred
     * precision, regardless of NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Timestamp)} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Timestamp readTimestamp(ResultSet rs, int index) throws SQLException {
        return rs.getTimestamp(index);
    }

    /**
     * Reads a time value from {@link ResultSet}. The corresponding SQL type is TIME, regardless of NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Time)} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Time readTime(ResultSet rs, int index) throws SQLException {
        return rs.getTime(index);
    }

    /**
     * Reads a date value from {@link ResultSet}. The corresponding SQL type is DATE, regardless of NOT NULL modifier.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Date)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Date readDate(ResultSet rs, int index) throws SQLException {
        return rs.getDate(index, Calendar.getInstance()); // MySQL driver behaves stupidly without calendar here
    }

    /**
     * Reads an array value from {@link ResultSet}.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Array)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @param <T>   array item class.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static <T> T[] readArray(ResultSet rs, int index) throws SQLException {
        Array array = rs.getArray(index);
        //noinspection unchecked
        return array != null ? (T[]) array.getArray() : null;
    }

    /**
     * Reads a binary data value from {@link ResultSet}. The corresponding PostgreSQL type is BYTEA.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(byte[])} method.
     * <b>Note:</b> MySQL blob support will be added later.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static byte[] readBinary(ResultSet rs, int index) throws SQLException {
        return rs.getBytes(index);
    }

    /**
     * Reads Java 8 instant value from {@link ResultSet}. The corresponding PostgreSQL type is TIMESTAMP WITH TIME ZONE
     * with preferred precision. The corresponding MySQL type is TIMESTAMP with preferred precision.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(Instant)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(Instant)} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static Instant readInstant(ResultSet rs, int index) throws SQLException {
        Timestamp timestamp = readTimestamp(rs, index);
        return timestamp != null ? timestamp.toInstant() : null;
    }

    /**
     * Reads Java 8 local date value from {@link ResultSet}. The corresponding SQL type is DATE.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(LocalDate)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(LocalDate)} method.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static LocalDate readLocalDate(ResultSet rs, int index) throws SQLException {
        Date date = readDate(rs, index);
        return date != null ? date.toLocalDate() : null;
    }

    /**
     * Reads Java 8 local time value from {@link ResultSet}. The corresponding SQL type is TIME.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(LocalTime)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(LocalTime)} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static LocalTime readLocalTime(ResultSet rs, int index) throws SQLException {
        return SquiggleUtils.deserialize(readTime(rs, index));
    }

    /**
     * Reads Java 8 local date/time value from {@link ResultSet}. The corresponding SQL type is TIMESTAMP with
     * preferred precision.
     * To write the value, use {@link com.github.squigglesql.squigglesql.parameter.Parameter#of(LocalDateTime)} or
     * {@link com.github.squigglesql.squigglesql.literal.Literal#of(LocalDateTime)} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param rs    result set.
     * @param index 1-based column index.
     * @return result value.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static LocalDateTime readLocalDateTime(ResultSet rs, int index) throws SQLException {
        Timestamp timestamp = readTimestamp(rs, index);
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    /**
     * Helper function that executes a selection query and:
     *
     * <ul>
     * <li>if the result set contains no rows, returns null.</li>
     * <li>if the result set contains exactly one row, maps it to Java model.</li>
     * <li>if the result set contains two or more rows, throws {@link TooManyRecordsException}.</li>
     * </ul>
     *
     * @param query      SQL selection query.
     * @param connection JDBC connection.
     * @param mapper     callback to map ResultSet to a Java model instance.
     * @param <T>        Java model class.
     * @return Java model instance or null.
     * @throws SQLException            if JDBC driver throws the exception.
     * @throws TooManyRecordsException if the result contains two or more rows.
     */
    public static <T> T selectOne(Query query, Connection connection, ResultMapper<T> mapper) throws SQLException {
        try (PreparedStatement statement = query.toStatement(new JdbcStatementCompiler(connection))) {
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                T result = mapper.apply(rs);
                if (rs.next()) {
                    throw new TooManyRecordsException();
                }
                return result;
            }
        }
    }

    /**
     * Helper function that executes a selection query and maps the result rows to Java models.
     *
     * @param query      SQL selection query.
     * @param connection JDBC connection.
     * @param mapper     callback to map ResultSet to a Java model instance.
     * @param <T>        Java model class.
     * @return list of Java model instances.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static <T> List<T> selectAll(Query query, Connection connection, ResultMapper<T> mapper) throws SQLException {
        try (PreparedStatement statement = query.toStatement(new JdbcStatementCompiler(connection))) {
            try (ResultSet rs = statement.executeQuery()) {
                List<T> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapper.apply(rs));
                }
                return result;
            }
        }
    }

    /**
     * Helper function that executes an insertion query and returns a generated key of the inserted row.
     *
     * @param query      SQL selection query.
     * @param connection JDBC connection.
     * @return list of Java model instances.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static int insert(Query query, Connection connection) throws SQLException {
        try (PreparedStatement statement = query.toStatement(new JdbcStatementCompiler(connection))) {
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    /**
     * Helper function that executes an update query and returns a number of updated rows.
     *
     * @param query      SQL selection query.
     * @param connection JDBC connection.
     * @return list of Java model instances.
     * @throws SQLException if JDBC driver throws the exception.
     */
    public static int update(Query query, Connection connection) throws SQLException {
        try (PreparedStatement statement = query.toStatement(new JdbcStatementCompiler(connection))) {
            return statement.executeUpdate();
        }
    }
}
