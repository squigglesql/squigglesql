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
package com.github.squigglesql.squigglesql.parameter;

import com.github.squigglesql.squigglesql.statement.Parametrized;

import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;

class TimeParameter extends Parameter {

    private final Time value;
    private final Calendar calendar;

    TimeParameter(Time value, Calendar calendar) {
        this.value = value;
        this.calendar = calendar;
    }

    @Override
    public void addValue(Parametrized builder) throws SQLException {
        builder.addTime(value, calendar);
    }
}
