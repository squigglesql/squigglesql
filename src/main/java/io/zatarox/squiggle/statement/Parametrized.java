package io.zatarox.squiggle.statement;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public interface Parametrized {

    void addNull(int sqlType) throws SQLException;

    void addBoolean(boolean value) throws SQLException;

    void addByte(byte value) throws SQLException;

    void addShort(short value) throws SQLException;

    void addInteger(int value) throws SQLException;

    void addLong(long value) throws SQLException;

    void addFloat(float value) throws SQLException;

    void addDouble(double value) throws SQLException;

    void addBigDecimal(BigDecimal value) throws SQLException;

    void addString(String value) throws SQLException;

    void addTimestamp(Timestamp value, Calendar calendar) throws SQLException;

    void addTime(Time value, Calendar calendar) throws SQLException;

    void addDate(Date value, Calendar calendar) throws SQLException;

    void addArray(Array value) throws SQLException;

    void addBytes(byte[] value) throws SQLException;
}
