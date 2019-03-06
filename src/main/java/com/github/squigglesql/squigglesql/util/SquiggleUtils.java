package com.github.squigglesql.squigglesql.util;

import java.sql.Time;
import java.time.LocalTime;

public abstract class SquiggleUtils {

    public static Time serialize(LocalTime value) {
        if (value == null) {
            return null;
        }
        // Time.valueOf ignores milliseconds for some stupid reason
        return new Time(Time.valueOf(value).getTime() + value.getNano() / 1000000);
    }

    public static LocalTime deserialize(Time value) {
        if (value == null) {
            return null;
        }
        // Time#toLocalTime ignores milliseconds for some stupid reason
        return value.toLocalTime().withNano((int) (value.getTime() % 1000) * 1000000);
    }
}
