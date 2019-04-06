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

import com.github.squigglesql.squigglesql.SquiggleConstants;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Time literal. Use {@link com.github.squigglesql.squigglesql.literal.Literal#of(java.time.LocalTime)} method to
 * instantiate it.
 */
public class TimeLiteral extends AbstractTimeLiteral {

    /**
     * Creates a literal.
     * @param value literal value.
     */
    public TimeLiteral(LocalTime value) {
        super(value);
    }

    @Override
    protected DateTimeFormatter getFormatter() {
        return SquiggleConstants.TIME_FORMATTER;
    }
}
