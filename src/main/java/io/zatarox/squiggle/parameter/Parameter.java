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
package io.zatarox.squiggle.parameter;

import io.zatarox.squiggle.Matchable;
import io.zatarox.squiggle.QueryCompiler;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.statement.Parametrized;
import io.zatarox.squiggle.util.SquiggleUtils;

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

public abstract class Parameter implements Matchable {

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write("?").addParameter(this);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
    }

    public abstract void addValue(Parametrized builder) throws SQLException;

    public static Parameter ofNull(int sqlType) {
        return new NullParameter(sqlType);
    }

    public static Parameter of(boolean value) {
        return value ? BooleanParameter.TRUE : BooleanParameter.FALSE;
    }

    public static Parameter of(Boolean value) {
        return value == null ? NullParameter.BOOLEAN : of(value.booleanValue());
    }

    public static Parameter of(byte value) {
        return new ByteParameter(value);
    }

    public static Parameter of(Byte value) {
        return value == null ? NullParameter.BYTE : new ByteParameter(value);
    }

    public static Parameter of(short value) {
        return new ShortParameter(value);
    }

    public static Parameter of(Short value) {
        return value == null ? NullParameter.SHORT : new ShortParameter(value);
    }

    public static Parameter of(int value) {
        return new IntegerParameter(value);
    }

    public static Parameter of(Integer value) {
        return value == null ? NullParameter.INTEGER : new IntegerParameter(value);
    }

    public static Parameter of(long value) {
        return new LongParameter(value);
    }

    public static Parameter of(Long value) {
        return value == null ? NullParameter.LONG : new LongParameter(value);
    }

    public static Parameter of(float value) {
        return new FloatParameter(value);
    }

    public static Parameter of(Float value) {
        return value == null ? NullParameter.FLOAT : new FloatParameter(value);
    }

    public static Parameter of(double value) {
        return new DoubleParameter(value);
    }

    public static Parameter of(Double value) {
        return value == null ? NullParameter.DOUBLE : new DoubleParameter(value);
    }

    public static Parameter of(BigDecimal value) {
        return new BigDecimalParameter(value);
    }

    public static Parameter of(String value) {
        return new StringParameter(value);
    }

    public static Parameter of(Timestamp value) {
        return of(value, null);
    }

    /**
     * @deprecated Some JDBC drivers don't give a shit about calendars, so use this method only if you know what you
     * are doing. The recommended way to proceed is to convert the timestamp to the local time zone manually before
     * sending it to the database, or use Java 8 date/time API Parameter.of methods.
     */
    public static Parameter of(Timestamp value, Calendar calendar) {
        return new TimestampParameter(value, calendar);
    }

    public static Parameter of(Time value) {
        return of(value, null);
    }

    /**
     * @deprecated Some JDBC drivers don't give a shit about calendars, so use this method only if you know what you
     * are doing. Also, it is in general not recommended to use TIME WITH TIME ZONE type, because some time zones have
     * a variable offset (e.g. all DST time zones), so the outcome can be unpredictable.
     */
    public static Parameter of(Time value, Calendar calendar) {
        return new TimeParameter(value, calendar);
    }

    public static Parameter of(Date value) {
        return of(value, null);
    }

    /**
     * @deprecated Some JDBC drivers don't give a shit about calendars, so use this method only if you know what you
     * are doing. Also, there is no DATE WITH TIME ZONE type in SQL standard, so, the existence of setDate method with
     * Calendar argument in JDBC PreparedStatement class is misleading.
     */
    public static Parameter of(Date value, Calendar calendar) {
        return new DateParameter(value, calendar);
    }

    public static Parameter of(Array value) {
        return new ArrayParameter(value);
    }

    public static Parameter of(byte[] value) {
        return new BytesParameter(value);
    }

    public static Parameter of(Instant value) {
        return value != null ? of(Timestamp.from(value)) : NullParameter.TIMESTAMP_WITH_TIMEZONE;
    }

    public static Parameter of(LocalDate value) {
        return value != null ? of(Date.valueOf(value)) : NullParameter.DATE;
    }

    public static Parameter of(LocalTime value) {
        return value != null ? of(SquiggleUtils.serialize(value)) : NullParameter.TIME;
    }

    public static Parameter of(LocalDateTime value) {
        return value != null ? of(Timestamp.valueOf(value)) : NullParameter.TIMESTAMP;
    }

    public static Parameter of(ZonedDateTime value) {
        return value != null ? of(Timestamp.from(value.toInstant())) : NullParameter.TIMESTAMP_WITH_TIMEZONE;
    }

    public static Parameter of(OffsetDateTime value) {
        return value != null ? of(Timestamp.from(value.toInstant())) : NullParameter.TIMESTAMP_WITH_TIMEZONE;
    }

    // TODO: Add OffsetTime support.
}
