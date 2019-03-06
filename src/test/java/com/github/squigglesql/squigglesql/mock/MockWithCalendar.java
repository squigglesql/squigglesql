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
package com.github.squigglesql.squigglesql.mock;

import java.util.Calendar;

public class MockWithCalendar {

    private final Object value;
    private final Calendar calendar;

    public MockWithCalendar(Object value, Calendar calendar) {
        this.value = value;
        this.calendar = calendar;
    }

    public Object getValue() {
        return value;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
