package com.github.squigglesql.squigglesql.util;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.query.Query;
import com.github.squigglesql.squigglesql.statement.JdbcStatementCompiler;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Byte) ? (Byte) obj : Byte.valueOf(rs.getByte(index));
    }

    public static short readShortNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getShort(index);
    }

    public static Short readShortNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Short) ? (Short) obj : Short.valueOf(rs.getShort(index));
    }

    public static int readIntegerNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getInt(index);
    }

    public static Integer readIntegerNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Integer) ? (Integer) obj : Integer.valueOf(rs.getInt(index));
    }

    public static long readLongNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getLong(index);
    }

    public static Long readLongNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Long) ? (Long) obj : Long.valueOf(rs.getLong(index));
    }

    public static float readFloatNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getFloat(index);
    }

    public static Float readFloatNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Float) ? (Float) obj : Float.valueOf(rs.getFloat(index));
    }

    public static double readDoubleNotNull(ResultSet rs, int index) throws SQLException {
        return rs.getDouble(index);
    }

    public static Double readDoubleNull(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        return (obj == null || obj instanceof Double) ? (Double) obj : Double.valueOf(rs.getDouble(index));
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
        return rs.getDate(index, Calendar.getInstance()); // MySQL driver behaves stupidly without calendar here
    }

    public static <T> T[] readArray(ResultSet rs, int index) throws SQLException {
        Array array = rs.getArray(index);
        //noinspection unchecked
        return array != null ? (T[]) array.getArray() : null;
    }

    public static byte[] readBinary(ResultSet rs, int index) throws SQLException {
        return rs.getBytes(index);
    }

    public static Instant readInstant(ResultSet rs, int index) throws SQLException {
        Timestamp timestamp = readTimestamp(rs, index);
        return timestamp != null ? timestamp.toInstant() : null;
    }

    public static LocalDate readLocalDate(ResultSet rs, int index) throws SQLException {
        Date date = readDate(rs, index);
        return date != null ? date.toLocalDate() : null;
    }

    public static LocalTime readLocalTime(ResultSet rs, int index) throws SQLException {
        return SquiggleUtils.deserialize(readTime(rs, index));
    }

    public static LocalDateTime readLocalDateTime(ResultSet rs, int index) throws SQLException {
        Timestamp timestamp = readTimestamp(rs, index);
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    public static <T> T selectOne(Query query, Connection connection, ResultMapper<T> mapper) throws SQLException {
        try (PreparedStatement statement = query.toStatement(new JdbcStatementCompiler(connection))) {
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                T result = mapper.apply(rs);
                if (rs.next()) {
                    throw new TooManyRecordsException();
                }
                return result;
            }
        }
    }

    public static <T> List<T> selectAll(Query query, Connection connection, ResultMapper<T> mapper) throws SQLException {
        try (PreparedStatement statement = query.toStatement(new JdbcStatementCompiler(connection))) {
            try (ResultSet rs = statement.executeQuery()) {
                List<T> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapper.apply(rs));
                }
                return result;
            }
        }
    }

    public static int insert(Query query, Connection connection) throws SQLException {
        try (PreparedStatement statement = query.toStatement(new JdbcStatementCompiler(connection))) {
            return statement.executeUpdate();
        }
    }

    public static void update(Query query, Connection connection) throws SQLException {
        insert(query, connection);
    }
}
