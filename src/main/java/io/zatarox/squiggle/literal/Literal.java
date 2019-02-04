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
package io.zatarox.squiggle.literal;

import io.zatarox.squiggle.Selectable;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.literal.time.DateLiteral;
import io.zatarox.squiggle.literal.time.InstantInterval;
import io.zatarox.squiggle.literal.time.TimeLiteral;
import io.zatarox.squiggle.literal.time.TimeWithTimeZoneLiteral;
import io.zatarox.squiggle.literal.time.TimestampLiteral;
import io.zatarox.squiggle.literal.time.TimestampWithTimeZoneLiteral;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Set;

public abstract class Literal implements Selectable {

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
    }

    public static Literal ofNull() {
        return RawLiteral.NULL;
    }

    public static Literal of(boolean value) {
        return value ? RawLiteral.TRUE : RawLiteral.FALSE;
    }

    public static Literal of(Boolean value) {
        return value == null ? RawLiteral.NULL : of(value.booleanValue());
    }

    public static Literal of(byte value) {
        return new RawLiteral(value);
    }

    public static Literal of(Byte value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    public static Literal of(short value) {
        return new RawLiteral(value);
    }

    public static Literal of(Short value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    public static Literal of(int value) {
        return new RawLiteral(value);
    }

    public static Literal of(Integer value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    public static Literal of(long value) {
        return new RawLiteral(value);
    }

    public static Literal of(Long value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    public static Literal of(float value) {
        return new RawLiteral(value);
    }

    public static Literal of(Float value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    public static Literal of(double value) {
        return new RawLiteral(value);
    }

    public static Literal of(Double value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    public static Literal of(BigDecimal value) {
        return value == null ? RawLiteral.NULL : new RawLiteral(value);
    }

    public static Literal of(Object value) {
        return value == null ? RawLiteral.NULL : new StringLiteral(value);
    }

    public static Literal of(Instant value) {
        return value == null ? RawLiteral.NULL : new InstantInterval(value);
    }

    public static Literal of(LocalDate value) {
        return value == null ? RawLiteral.NULL : new DateLiteral(value);
    }

    public static Literal of(LocalTime value) {
        return value == null ? RawLiteral.NULL : new TimeLiteral(value);
    }

    public static Literal of(LocalDateTime value) {
        return value == null ? RawLiteral.NULL : new TimestampLiteral(value);
    }

    public static Literal of(ZonedDateTime value) {
        return value == null ? RawLiteral.NULL : new TimestampWithTimeZoneLiteral(value);
    }

    public static Literal of(OffsetDateTime value) {
        return value == null ? RawLiteral.NULL : new TimestampWithTimeZoneLiteral(value);
    }

    public static Literal of(OffsetTime value) {
        return value == null ? RawLiteral.NULL : new TimeWithTimeZoneLiteral(value);
    }

    public static Literal unsafe(String sql) {
        return sql == null ? RawLiteral.NULL : new RawLiteral(sql);
    }

    // TODO: array, byte[], Date/Time (with or without timezone)
}
