package io.zatarox.squiggle.util;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public abstract class JdbcUtils {

    public static boolean readBooleanNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getBoolean(index);
    }

    public static Boolean readBooleanNull(ResultSet rs, int index) throws SQLException {
        return (Boolean) rs.getObject(index);
    }

    public static byte readByteNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getByte(index);
    }

    public static Byte readByteNull(ResultSet rs, int index) throws SQLException {
        Integer obj = (Integer) rs.getObject(index);
        return obj != null ? obj.byteValue() : null;
    }

    public static short readShortNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getShort(index);
    }

    public static Short readShortNull(ResultSet rs, int index) throws SQLException {
        Integer obj = (Integer) rs.getObject(index);
        return obj != null ? obj.shortValue() : null;
    }

    public static int readIntegerNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getInt(index);
    }

    public static Integer readIntegerNull(ResultSet rs, int index) throws SQLException {
        return (Integer) rs.getObject(index);
    }

    public static long readLongNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getLong(index);
    }

    public static Long readLongNull(ResultSet rs, int index) throws SQLException {
        return (Long) rs.getObject(index);
    }

    public static float readFloatNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getFloat(index);
    }

    public static Float readFloatNull(ResultSet rs, int index) throws SQLException {
        return (Float) rs.getObject(index);
    }

    public static double readDoubleNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getDouble(index);
    }

    public static Double readDoubleNull(ResultSet rs, int index) throws SQLException {
        return (Double) rs.getObject(index);
    }

    public static BigDecimal readBigDecimal(ResultSet rs, int index) throws SQLException {
        return rs.getBigDecimal(index);
    }

    public static String readString(ResultSet rs, int index) throws SQLException {
        return rs.getString(index);
    }

    public static Timestamp readTimestamp(ResultSet rs, int index) throws SQLException {
        return rs.getTimestamp(index);
    }

    public static Time readTime(ResultSet rs, int index) throws SQLException {
        return rs.getTime(index);
    }

    public static Date readDate(ResultSet rs, int index) throws SQLException {
        return rs.getDate(index);
    }

    public static <T> T[] readArray(ResultSet rs, int index) throws SQLException {
        Array array = rs.getArray(index);
        //noinspection unchecked
        return array != null ? (T[]) array.getArray() : null;
    }

    public static byte[] readBinary(ResultSet rs, int index) throws SQLException {
        return rs.getBytes(index);
    }
}
