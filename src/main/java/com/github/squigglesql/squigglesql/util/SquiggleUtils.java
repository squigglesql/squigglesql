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

import java.sql.Time;
import java.time.LocalTime;

/**
 * Various internal utilities.
 */
public abstract class SquiggleUtils {

    /**
     * Converts {@link LocalTime} to {@link Time};
     *
     * @param value value to convert.
     * @return the converted value.
     */
    public static Time serialize(LocalTime value) {
        if (value == null) {
            return null;
        }
        // Time.valueOf ignores milliseconds for some stupid reason
        return new Time(Time.valueOf(value).getTime() + value.getNano() / 1000000);
    }

    /**
     * Converts {@link Time} to {@link LocalTime};
     *
     * @param value value to convert.
     * @return the converted value.
     */
    public static LocalTime deserialize(Time value) {
        if (value == null) {
            return null;
        }
        // Time#toLocalTime ignores milliseconds for some stupid reason
        return value.toLocalTime().withNano((int) (value.getTime() % 1000) * 1000000);
    }
}
