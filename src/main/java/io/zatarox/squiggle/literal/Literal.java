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

import java.math.BigDecimal;
import java.util.Set;

public abstract class Literal implements Selectable {

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
    }

    public static Literal ofNull() {
        return NullLiteral.INSTANCE;
    }

    public static Literal of(boolean value) {
        return value ? RawLiteral.TRUE : RawLiteral.FALSE;
    }

    public static Literal of(Boolean value) {
        return value == null ? NullLiteral.INSTANCE : of(value.booleanValue());
    }

    public static Literal of(byte value) {
        return new RawLiteral(value);
    }

    public static Literal of(Byte value) {
        return value == null ? NullLiteral.INSTANCE : new RawLiteral(value);
    }

    public static Literal of(short value) {
        return new RawLiteral(value);
    }

    public static Literal of(Short value) {
        return value == null ? NullLiteral.INSTANCE : new RawLiteral(value);
    }

    public static Literal of(int value) {
        return new RawLiteral(value);
    }

    public static Literal of(Integer value) {
        return value == null ? NullLiteral.INSTANCE : new RawLiteral(value);
    }

    public static Literal of(long value) {
        return new RawLiteral(value);
    }

    public static Literal of(Long value) {
        return value == null ? NullLiteral.INSTANCE : new RawLiteral(value);
    }

    public static Literal of(float value) {
        return new RawLiteral(value);
    }

    public static Literal of(Float value) {
        return value == null ? NullLiteral.INSTANCE : new RawLiteral(value);
    }

    public static Literal of(double value) {
        return new RawLiteral(value);
    }

    public static Literal of(Double value) {
        return value == null ? NullLiteral.INSTANCE : new RawLiteral(value);
    }

    public static Literal of(BigDecimal value) {
        return value == null ? NullLiteral.INSTANCE : new RawLiteral(value);
    }

    public static Literal of(String value) {
        return value == null ? NullLiteral.INSTANCE : new StringLiteral(value);
    }

    public static Literal unsafe(String sql) {
        return sql == null ? NullLiteral.INSTANCE : new RawLiteral(sql);
    }

    // TODO: byte[], Date/Time (with or without timezone)
}
