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
package com.github.squigglesql.squigglesql.parameter;

import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.statement.Parametrized;
import com.github.squigglesql.squigglesql.util.SquiggleUtils;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Set;

/**
 * Abstract SQL parameter. Parameters are a preferred way to pass variables to an SQL query. As opposed to
 * {@link com.github.squigglesql.squigglesql.literal.Literal}, Parameter gets serialized to a constant "?"
 * symbol, and its actual value gets transferred to the database directly via the JDBC driver. This approach is:
 *
 * <ol>
 * <li>more reliable, as you don't need to escape the values in the SQL query;</li>
 * <li>safer, as confidential data doesn't get written into log files;</li>
 * <li>faster, as the database is able to cache the compiled query structure and plan.</li>
 * </ol>
 * Squiggle SQL has a bunch of built-in parameter types instantiated with "of" static methods. You can invent
 * custom parameter types by extending Parameter class and implementing {@link Parameter#addValue(Parametrized)} method.
 */
public abstract class Parameter implements Matchable {

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write("?").addParameter(this);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
    }

    /**
     * Transfers the value of this parameter to a JDBC driver.
     *
     * @param statement statement adapter.
     * @throws SQLException if JDBC driver throws it.
     */
    public abstract void addValue(Parametrized statement) throws SQLException;

    /**
     * Instantiates a parameter with null value. You must specify the JDBC value type explicitly. The preferred way to
     * add a null value is to call "of" method of a specific type and pass null there.
     *
     * @param sqlType the SQL type code defined in <code>java.sql.Types</code>
     * @return parameter to use in a query.
     */
    public static Parameter ofNull(int sqlType) {
        return new NullParameter(sqlType);
    }

    /**
     * Instantiates a boolean parameter. The corresponding SQL type is BOOLEAN with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readBooleanNotNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(boolean value) {
        return value ? BooleanParameter.TRUE : BooleanParameter.FALSE;
    }

    /**
     * Instantiates a boolean parameter. The corresponding SQL type is BOOLEAN with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readBooleanNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(Boolean value) {
        return value == null ? NullParameter.BOOLEAN : of(value.booleanValue());
    }

    /**
     * Instantiates a byte parameter. The corresponding SQL type is SMALLINT with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readByteNotNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(byte value) {
        return new ByteParameter(value);
    }

    /**
     * Instantiates a byte parameter. The corresponding SQL type is SMALLINT with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readByteNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(Byte value) {
        return value == null ? NullParameter.BYTE : new ByteParameter(value);
    }

    /**
     * Instantiates a short integer parameter. The corresponding SQL type is SMALLINT with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readShortNotNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(short value) {
        return new ShortParameter(value);
    }

    /**
     * Instantiates a short integer parameter. The corresponding SQL type is SMALLINT with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readShortNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(Short value) {
        return value == null ? NullParameter.SHORT : new ShortParameter(value);
    }

    /**
     * Instantiates an integer parameter. The corresponding SQL type is INT with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readIntegerNotNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(int value) {
        return new IntegerParameter(value);
    }

    /**
     * Instantiates an integer parameter. The corresponding SQL type is INT with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readIntegerNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(Integer value) {
        return value == null ? NullParameter.INTEGER : new IntegerParameter(value);
    }

    /**
     * Instantiates a long integer parameter. The corresponding SQL type is BIGINT with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLongNotNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(long value) {
        return new LongParameter(value);
    }

    /**
     * Instantiates a long integer parameter. The corresponding SQL type is BIGINT with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLongNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(Long value) {
        return value == null ? NullParameter.LONG : new LongParameter(value);
    }

    /**
     * Instantiates a floating point number parameter. The corresponding SQL type is REAL with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readFloatNotNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(float value) {
        return new FloatParameter(value);
    }

    /**
     * Instantiates a floating point number parameter. The corresponding SQL type is REAL with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readFloatNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(Float value) {
        return value == null ? NullParameter.FLOAT : new FloatParameter(value);
    }

    /**
     * Instantiates a double precision floating point number parameter. The corresponding SQL type
     * is DOUBLE PRECISION with NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readDoubleNotNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(double value) {
        return new DoubleParameter(value);
    }

    /**
     * Instantiates a double precision floating point number parameter. The corresponding SQL type
     * is DOUBLE PRECISION with NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readDoubleNull} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(Double value) {
        return value == null ? NullParameter.DOUBLE : new DoubleParameter(value);
    }

    /**
     * Instantiates a big decimal parameter. The corresponding SQL type is NUMERIC with a preferred precision,
     * regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readBigDecimal} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(BigDecimal value) {
        return new BigDecimalParameter(value);
    }

    /**
     * Instantiates a string parameter. The corresponding SQL type is TEXT, regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readString} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(String value) {
        return new StringParameter(value);
    }

    /**
     * Instantiates a timestamp parameter. The corresponding SQL type is TIMESTAMP with a preferred precision,
     * regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readTimestamp} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     * @deprecated Consider using Java 8 date/time API Parameter.of methods.
     */
    public static Parameter of(Timestamp value) {
        return of(value, null);
    }

    /**
     * Instantiates a timestamp parameter. The corresponding SQL type is TIMESTAMP with a preferred precision,
     * regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readTimestamp} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value    parameter value.
     * @param calendar calendar to use. See setTimestamp method of JDBC PreparedStatement for details.
     * @return parameter to use in a query.
     * @deprecated Some JDBC drivers ignore calendars, so use this method only if you know what you
     * are doing. The recommended way to proceed is to convert the timestamp to the local time zone manually before
     * sending it to the database, or use Java 8 date/time API Parameter.of methods.
     */
    public static Parameter of(Timestamp value, Calendar calendar) {
        return new TimestampParameter(value, calendar);
    }

    /**
     * Instantiates a time parameter. The corresponding SQL type is TIME, regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readTimestamp} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     * @deprecated Consider using Java 8 date/time API Parameter.of methods.
     */
    public static Parameter of(Time value) {
        return of(value, null);
    }

    /**
     * Instantiates a time parameter. The corresponding SQL type is TIME, regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readTime} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value    parameter value.
     * @param calendar calendar to use. See setTimestamp method of JDBC PreparedStatement for details.
     * @return parameter to use in a query.
     * @deprecated Some JDBC drivers ignore calendars, so use this method only if you know what you
     * are doing. Also, it is in general not recommended to use TIME WITH TIME ZONE type, because some time zones have
     * a variable offset (e.g. all DST time zones), so the outcome can be unpredictable.
     */
    public static Parameter of(Time value, Calendar calendar) {
        return new TimeParameter(value, calendar);
    }

    /**
     * Instantiates a date parameter. The corresponding SQL type is DATE, regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readDate} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     * @deprecated Consider using Java 8 date/time API Parameter.of methods.
     */
    public static Parameter of(Date value) {
        return of(value, Calendar.getInstance()); // MySQL driver behaves stupidly without calendar here
    }

    /**
     * Instantiates a date parameter. The corresponding SQL type is DATE, regardless of NOT NULL modifier.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readDate} method.
     *
     * @param value    parameter value.
     * @param calendar calendar to use. See setTimestamp method of JDBC PreparedStatement for details.
     * @return parameter to use in a query.
     * @deprecated Some JDBC drivers don't give a shit about calendars, so use this method only if you know what you
     * are doing. Also, there is no DATE WITH TIME ZONE type in SQL standard, so, the existence of setDate method with
     * Calendar argument in JDBC PreparedStatement class is misleading.
     */
    public static Parameter of(Date value, Calendar calendar) {
        return new DateParameter(value, calendar);
    }

    /**
     * Instantiates an array parameter.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readArray} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(Array value) {
        return new ArrayParameter(value);
    }

    /**
     * Instantiates a binary data parameter. The corresponding PostgreSQL type is BYTEA.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readBinary} method.
     * <b>Note:</b> MySQL blob support will be added later.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(byte[] value) {
        return new BytesParameter(value);
    }

    /**
     * Instantiates Java 8 instant parameter. The corresponding PostgreSQL type is TIMESTAMP WITH TIME ZONE with
     * preferred precision. The corresponding MySQL type is TIMESTAMP with preferred precision.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readInstant} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(Instant value) {
        return value != null ? of(Timestamp.from(value)) : NullParameter.TIMESTAMP_WITH_TIMEZONE;
    }

    /**
     * Instantiates Java 8 local date parameter. The corresponding SQL type is DATE.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLocalDate} method.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(LocalDate value) {
        return value != null ? of(Date.valueOf(value)) : NullParameter.DATE;
    }

    /**
     * Instantiates Java 8 local time parameter. The corresponding SQL type is TIME.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLocalTime} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(LocalTime value) {
        return of(SquiggleUtils.serialize(value));
    }

    /**
     * Instantiates Java 8 local date/time parameter. The corresponding SQL type is TIMESTAMP with preferred precision.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readLocalDateTime} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(LocalDateTime value) {
        return value != null ? of(Timestamp.valueOf(value)) : NullParameter.TIMESTAMP;
    }

    /**
     * Instantiates Java 8 zoned date/time parameter. The corresponding PostgreSQL type is TIMESTAMP WITH TIME ZONE
     * with preferred precision. The corresponding MySQL type is TIMESTAMP with preferred precision.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readInstant} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     * <b>Note:</b> None of the databases stores the exact time zone identifier. That's why all you can get back
     * is an Instant.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(ZonedDateTime value) {
        return value != null ? of(Timestamp.from(value.toInstant())) : NullParameter.TIMESTAMP_WITH_TIMEZONE;
    }

    /**
     * Instantiates Java 8 offset date/time parameter. The corresponding PostgreSQL type is TIMESTAMP WITH TIME ZONE
     * with preferred precision. The corresponding MySQL type is TIMESTAMP with preferred precision.
     * To read the value, use {@link com.github.squigglesql.squigglesql.util.JdbcUtils#readInstant} method.
     * <b>Note:</b> MySQL doesn't support milliseconds. Be careful.
     * <b>Note:</b> None of the databases stores the exact time zone identifier. That's why all you can get back is an Instant.
     *
     * @param value parameter value.
     * @return parameter to use in a query.
     */
    public static Parameter of(OffsetDateTime value) {
        return value != null ? of(Timestamp.from(value.toInstant())) : NullParameter.TIMESTAMP_WITH_TIMEZONE;
    }

    // TODO: Add OffsetTime support.
}
