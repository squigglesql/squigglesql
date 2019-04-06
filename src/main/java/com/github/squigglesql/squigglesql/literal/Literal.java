/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.literal;

import com.github.squigglesql.squigglesql.Selectable;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.literal.time.DateLiteral;
import com.github.squigglesql.squigglesql.literal.time.InstantInterval;
import com.github.squigglesql.squigglesql.literal.time.TimeLiteral;
import com.github.squigglesql.squigglesql.literal.time.TimeWithTimeZoneLiteral;
import com.github.squigglesql.squigglesql.literal.time.TimestampLiteral;
import com.github.squigglesql.squigglesql.literal.time.TimestampWithTimeZoneLiteral;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Literal is a constant value in an SQL query. Use literals only to specify constant values - for variables, use
 * {@link com.github.squigglesql.squigglesql.parameter.Parameter} (see its documentation for details).
 * Squiggle SQL has a bunch of built-in literal types instantiated with <tt>of</tt> static methods. You can invent
 * custom literal types by extending {@link AbstractStringLiteral} or using {@link RawLiteral}.
 */
public abstract class Literal implements Selectable {

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
    }

    /**
     * Returns SQL NULL literal.
     *
     * @return literal to use in a query.
     */
    public static Literal ofNull() {
        return RawLiteral.NULL;
    }

    /**
     * Returns SQL boolean literal (TRUE or FALSE). The corresponding SQL type is BOOLEAN with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readBooleanNotNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(boolean value) {
        return value ? RawLiteral.TRUE : RawLiteral.FALSE;
    }

    /**
     * Returns SQL boolean literal (TRUE, FALSE or NULL). The corresponding SQL type is BOOLEAN with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readBooleanNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(Boolean value) {
        return value == null ? RawLiteral.NULL : of(value.booleanValue());
    }

    /**
     * Returns SQL integer literal. The corresponding SQL type is SMALLINT with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readByteNotNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(byte value) {
        return new RawLiteral(value);
    }

    /**
     * Returns SQL integer literal. The corresponding SQL type is SMALLINT with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readByteNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(Byte value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    /**
     * Returns SQL integer literal. The corresponding SQL type is SMALLINT with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readShortNotNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(short value) {
        return new RawLiteral(value);
    }

    /**
     * Returns SQL integer literal. The corresponding SQL type is SMALLINT with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readShortNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(Short value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    /**
     * Returns SQL integer literal. The corresponding SQL type is INT with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readIntegerNotNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(int value) {
        return new RawLiteral(value);
    }

    /**
     * Returns SQL integer literal. The corresponding SQL type is INT with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readIntegerNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(Integer value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    /**
     * Returns SQL integer literal. The corresponding SQL type is BIGINT with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLongNotNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(long value) {
        return new RawLiteral(value);
    }

    /**
     * Returns SQL integer literal. The corresponding SQL type is BIGINT with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLongNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(Long value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    /**
     * Returns SQL real number literal. The corresponding SQL type is REAL with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readFloatNotNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(float value) {
        return new RawLiteral(value);
    }

    /**
     * Returns SQL real number literal. The corresponding SQL type is REAL with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readFloatNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(Float value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    /**
     * Returns SQL real number literal. The corresponding SQL type is DOUBLE PRECISION with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readDoubleNotNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(double value) {
        return new RawLiteral(value);
    }

    /**
     * Returns SQL real number literal. The corresponding SQL type is DOUBLE PRECISION with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readDoubleNull} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(Double value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    /**
     * Returns SQL integer literal. The corresponding SQL type is NUMERIC with a preferred precision,
     * regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readBigDecimal} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(BigDecimal value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    /**
     * Returns SQL string literal. The corresponding SQL type is TEXT, regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readString} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(Object value) {
        return value == null ? RawLiteral.NULL : new StringLiteral(value);
    }

    /**
     * Returns SQL date/time string literal. The corresponding PostgreSQL type is TIMESTAMP WITH TIME ZONE with
     * preferred precision. The corresponding MySQL type is TIMESTAMP with preferred precision.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readInstant} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(Instant value) {
        return value == null ? RawLiteral.NULL : new InstantInterval(value);
    }

    /**
     * Returns SQL date string literal. The corresponding SQL type is DATE.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLocalDate} method.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(LocalDate value) {
        return value == null ? RawLiteral.NULL : new DateLiteral(value);
    }

    /**
     * Returns SQL time string literal. The corresponding SQL type is TIME.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLocalTime} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(LocalTime value) {
        return value == null ? RawLiteral.NULL : new TimeLiteral(value);
    }

    /**
     * Returns SQL date/time string literal. The corresponding SQL type is TIMESTAMP with preferred precision.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLocalDateTime} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(LocalDateTime value) {
        return value == null ? RawLiteral.NULL : new TimestampLiteral(value);
    }

    /**
     * Returns SQL date/time string literal. The corresponding PostgreSQL type is TIMESTAMP WITH TIME ZONE
     * with preferred precision. The corresponding MySQL type is TIMESTAMP with preferred precision.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readInstant} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     * <b>Note:</b> None of the databases stores the exact time zone identifier. That's why all you can get back
     * is an Instant.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(ZonedDateTime value) {
        return value == null ? RawLiteral.NULL : new TimestampWithTimeZoneLiteral(value);
    }

    /**
     * Returns SQL date/time string literal. The corresponding PostgreSQL type is TIMESTAMP WITH TIME ZONE
     * with preferred precision. The corresponding MySQL type is TIMESTAMP with preferred precision.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readInstant} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     * <b>Note:</b> None of the databases stores the exact time zone identifier. That's why all you can get back is an Instant.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(OffsetDateTime value) {
        return value == null ? RawLiteral.NULL : new TimestampWithTimeZoneLiteral(value);
    }

    /**
     * Returns SQL time string literal. The corresponding SQL type is TIME.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLocalTime} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value literal value.
     * @return literal to use in a query.
     */
    public static Literal of(OffsetTime value) {
        return value == null ? RawLiteral.NULL : new TimeWithTimeZoneLiteral(value);
    }

    /**
     * Returns an unsafe raw literal. If you add it to a query, the compiler will simply dump <tt>sql</tt> parameter
     * value as a part of the output. Be careful when you use this kind of literal.
     *
     * @param sql part of SQL query to dump as a literal.
     * @return literal to use in a query.
     */
    public static Literal unsafe(String sql) {
        return sql == null ? RawLiteral.NULL : new RawLiteral(sql);
    }
}
