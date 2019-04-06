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
package com.github.squigglesql.squigglesql.literal.time;

import com.github.squigglesql.squigglesql.literal.AbstractStringLiteral;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Abstract date/time string literal.
 */
public abstract class AbstractTimeLiteral extends AbstractStringLiteral {

    private final TemporalAccessor value;

    /**
     * Creates a literal.
     *
     * @param value literal value.
     */
    public AbstractTimeLiteral(TemporalAccessor value) {
        this.value = value;
    }

    @Override
    protected Object getValue() {
        return getFormatter().format(value);
    }

    /**
     * @return date/time formatter to write the value to the output.
     */
    protected abstract DateTimeFormatter getFormatter();
}
