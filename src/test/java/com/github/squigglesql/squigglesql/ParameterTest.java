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
package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.databases.TestDatabaseColumn;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.statement.JdbcStatementCompiler;
import com.github.squigglesql.squigglesql.util.JdbcUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static com.github.squigglesql.squigglesql.TestUtils.*;
import static org.junit.Assert.*;

public class ParameterTest {

    private static final boolean BOOLEAN = true;
    private static final byte BYTE = 10;
    private static final short SHORT = 1000;
    private static final int INT = 1000000;
    private static final long LONG = 1000000000000L;
    private static final float FLOAT = 10.5f;
    private static final double DOUBLE = 20.5d;
    private static final BigDecimal BIG_DECIMAL = new BigDecimal(LONG).pow(4);
    private static final String STRING = "Hello";

    // We don't test parameters in conjunction with Calendars. All the related Parameter.of methods are discouraged
    // to use and marked as deprecated. See deprecation comments for details. Use Java 8 date/time API Parameter.of
    // methods as a better alternative. They perform all the necessary time zone conversions automatically.
    private static final Timestamp TIMESTAMP = Timestamp.valueOf("2018-01-02 03:04:05.006");
    private static final Time TIME = new Time(Time.valueOf("03:04:05").getTime() + 6); // 03:04:05.006
    private static final Date DATE = Date.valueOf("2018-01-02");

    private static final Object[] INT_ARRAY = new Object[]{3, 4, 5};
    private static final Object[] STRING_ARRAY = new Object[]{"a", "b", "c"};
    private static final byte[] BINARY = new byte[]{60, 40, -55, 22, 127, -128, 0, 33};

    private static final Instant INSTANT = Instant.parse("2018-01-02T03:04:05.006Z");
    private static final LocalDate LOCAL_DATE = LocalDate.of(2018, 1, 2);
    private static final LocalTime LOCAL_TIME = LocalTime.of(3, 4, 5, 6000000);
    private static final LocalDateTime LOCAL_DATE_TIME =
            LocalDateTime.of(2018, 1, 2, 3, 4, 5, 6000000);
    private static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.of(LOCAL_DATE_TIME, ZoneId.of("America/Vancouver"));
    private static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime.of(LOCAL_DATE_TIME, ZoneOffset.ofHours(-8));

    private static final Table TABLE = new Table("test_table");
    private static final TableColumn BOOLEAN_COLUMN = TABLE.get("boolean_c");
    private static final TableColumn BYTE_COLUMN = TABLE.get("byte_c");
    private static final TableColumn SHORT_COLUMN = TABLE.get("short_c");
    private static final TableColumn INT_COLUMN = TABLE.get("int_c");
    private static final TableColumn LONG_COLUMN = TABLE.get("long_c");
    private static final TableColumn FLOAT_COLUMN = TABLE.get("float_c");
    private static final TableColumn DOUBLE_COLUMN = TABLE.get("double_c");
    private static final TableColumn BIG_DECIMAL_COLUMN = TABLE.get("big_decimal_c");
    private static final TableColumn STRING_COLUMN = TABLE.get("string_c");

    private static final TableColumn FLOAT_INFINITY_COLUMN = TABLE.get("float_infinity_c");
    private static final TableColumn FLOAT_MINUS_INFINITY_COLUMN = TABLE.get("float_minus_infinity_c");
    private static final TableColumn FLOAT_NAN_COLUMN = TABLE.get("float_nan_c");

    private static final TableColumn DOUBLE_INFINITY_COLUMN = TABLE.get("double_infinity_c");
    private static final TableColumn DOUBLE_MINUS_INFINITY_COLUMN = TABLE.get("double_minus_infinity_c");
    private static final TableColumn DOUBLE_NAN_COLUMN = TABLE.get("double_nan_c");

    private static final TableColumn TIMESTAMP_COLUMN = TABLE.get("timestamp_c");
    private static final TableColumn TIME_COLUMN = TABLE.get("time_c");
    private static final TableColumn DATE_COLUMN = TABLE.get("date_c");

    private static final TableColumn INT_ARRAY_COLUMN = TABLE.get("int_array_c");
    private static final TableColumn STRING_ARRAY_COLUMN = TABLE.get("string_array_c");
    private static final TableColumn BINARY_COLUMN = TABLE.get("binary_c");

    private static final TableColumn INSTANT_COLUMN = TABLE.get("instant_c");
    private static final TableColumn LOCAL_DATE_COLUMN = TABLE.get("local_date_c");
    private static final TableColumn LOCAL_TIME_COLUMN = TABLE.get("local_time_c");
    private static final TableColumn LOCAL_DATE_TIME_COLUMN = TABLE.get("local_date_time_c");
    private static final TableColumn ZONED_DATE_TIME_COLUMN = TABLE.get("zoned_date_time_c");
    private static final TableColumn OFFSET_DATE_TIME_COLUMN = TABLE.get("offset_date_time_c");

    @Test
    public void testRegularNotNullByDefinition() throws SQLException {
        withRegularTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(BOOLEAN_COLUMN, Parameter.of(BOOLEAN));
            insert.addValue(BYTE_COLUMN, Parameter.of(BYTE));
            insert.addValue(SHORT_COLUMN, Parameter.of(SHORT));
            insert.addValue(INT_COLUMN, Parameter.of(INT));
            insert.addValue(LONG_COLUMN, Parameter.of(LONG));
            insert.addValue(FLOAT_COLUMN, Parameter.of(FLOAT));
            insert.addValue(DOUBLE_COLUMN, Parameter.of(DOUBLE));
            insert.addValue(BIG_DECIMAL_COLUMN, Parameter.of(BIG_DECIMAL));
            insert.addValue(STRING_COLUMN, Parameter.of(STRING));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn booleanResult = select.addToSelection(e.get(BOOLEAN_COLUMN));
            ResultColumn byteResult = select.addToSelection(e.get(BYTE_COLUMN));
            ResultColumn shortResult = select.addToSelection(e.get(SHORT_COLUMN));
            ResultColumn intResult = select.addToSelection(e.get(INT_COLUMN));
            ResultColumn longResult = select.addToSelection(e.get(LONG_COLUMN));
            ResultColumn floatResult = select.addToSelection(e.get(FLOAT_COLUMN));
            ResultColumn doubleResult = select.addToSelection(e.get(DOUBLE_COLUMN));
            ResultColumn bigDecimalResult = select.addToSelection(e.get(BIG_DECIMAL_COLUMN));
            ResultColumn stringResult = select.addToSelection(e.get(STRING_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals(BOOLEAN, JdbcUtils.readBooleanNotNull(rs, booleanResult.getIndex()));
                    assertEquals(BYTE, JdbcUtils.readByteNotNull(rs, byteResult.getIndex()));
                    assertEquals(SHORT, JdbcUtils.readShortNotNull(rs, shortResult.getIndex()));
                    assertEquals(INT, JdbcUtils.readIntegerNotNull(rs, intResult.getIndex()));
                    assertEquals(LONG, JdbcUtils.readLongNotNull(rs, longResult.getIndex()));
                    assertEquals(FLOAT, JdbcUtils.readFloatNotNull(rs, floatResult.getIndex()), 0.0f);
                    assertEquals(DOUBLE, JdbcUtils.readDoubleNotNull(rs, doubleResult.getIndex()), 0.0d);
                    assertEquals(BIG_DECIMAL, JdbcUtils.readBigDecimal(rs, bigDecimalResult.getIndex()));
                    assertEquals(STRING, JdbcUtils.readString(rs, stringResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, true);
    }

    @Test
    public void testRegularNotNullByValue() throws SQLException {
        withRegularTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(BOOLEAN_COLUMN, Parameter.of(BOOLEAN));
            insert.addValue(BYTE_COLUMN, Parameter.of(BYTE));
            insert.addValue(SHORT_COLUMN, Parameter.of(SHORT));
            insert.addValue(INT_COLUMN, Parameter.of(INT));
            insert.addValue(LONG_COLUMN, Parameter.of(LONG));
            insert.addValue(FLOAT_COLUMN, Parameter.of(FLOAT));
            insert.addValue(DOUBLE_COLUMN, Parameter.of(DOUBLE));
            insert.addValue(BIG_DECIMAL_COLUMN, Parameter.of(BIG_DECIMAL));
            insert.addValue(STRING_COLUMN, Parameter.of(STRING));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn booleanResult = select.addToSelection(e.get(BOOLEAN_COLUMN));
            ResultColumn byteResult = select.addToSelection(e.get(BYTE_COLUMN));
            ResultColumn shortResult = select.addToSelection(e.get(SHORT_COLUMN));
            ResultColumn intResult = select.addToSelection(e.get(INT_COLUMN));
            ResultColumn longResult = select.addToSelection(e.get(LONG_COLUMN));
            ResultColumn floatResult = select.addToSelection(e.get(FLOAT_COLUMN));
            ResultColumn doubleResult = select.addToSelection(e.get(DOUBLE_COLUMN));
            ResultColumn bigDecimalResult = select.addToSelection(e.get(BIG_DECIMAL_COLUMN));
            ResultColumn stringResult = select.addToSelection(e.get(STRING_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals(BOOLEAN, JdbcUtils.readBooleanNull(rs, booleanResult.getIndex()));
                    assertEquals((Byte) BYTE, JdbcUtils.readByteNull(rs, byteResult.getIndex()));
                    assertEquals((Short) SHORT, JdbcUtils.readShortNull(rs, shortResult.getIndex()));
                    assertEquals((Integer) INT, JdbcUtils.readIntegerNull(rs, intResult.getIndex()));
                    assertEquals((Long) LONG, JdbcUtils.readLongNull(rs, longResult.getIndex()));
                    assertEquals(FLOAT, JdbcUtils.readFloatNull(rs, floatResult.getIndex()), 0.0f);
                    assertEquals(DOUBLE, JdbcUtils.readDoubleNull(rs, doubleResult.getIndex()), 0.0d);
                    assertEquals(BIG_DECIMAL, rs.getBigDecimal(bigDecimalResult.getIndex()));
                    assertEquals(STRING, rs.getString(stringResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    @Test
    public void testRegularNull() throws SQLException {
        withRegularTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(BOOLEAN_COLUMN, Parameter.of((Boolean) null));
            insert.addValue(BYTE_COLUMN, Parameter.of((Byte) null));
            insert.addValue(SHORT_COLUMN, Parameter.of((Short) null));
            insert.addValue(INT_COLUMN, Parameter.of((Integer) null));
            insert.addValue(LONG_COLUMN, Parameter.of((Long) null));
            insert.addValue(FLOAT_COLUMN, Parameter.of((Float) null));
            insert.addValue(DOUBLE_COLUMN, Parameter.of((Double) null));
            insert.addValue(BIG_DECIMAL_COLUMN, Parameter.of((BigDecimal) null));
            insert.addValue(STRING_COLUMN, Parameter.of((String) null));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn booleanResult = select.addToSelection(e.get(BOOLEAN_COLUMN));
            ResultColumn byteResult = select.addToSelection(e.get(BYTE_COLUMN));
            ResultColumn shortResult = select.addToSelection(e.get(SHORT_COLUMN));
            ResultColumn intResult = select.addToSelection(e.get(INT_COLUMN));
            ResultColumn longResult = select.addToSelection(e.get(LONG_COLUMN));
            ResultColumn floatResult = select.addToSelection(e.get(FLOAT_COLUMN));
            ResultColumn doubleResult = select.addToSelection(e.get(DOUBLE_COLUMN));
            ResultColumn bigDecimalResult = select.addToSelection(e.get(BIG_DECIMAL_COLUMN));
            ResultColumn stringResult = select.addToSelection(e.get(STRING_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertNull(JdbcUtils.readBooleanNull(rs, booleanResult.getIndex()));
                    assertNull(JdbcUtils.readByteNull(rs, byteResult.getIndex()));
                    assertNull(JdbcUtils.readShortNull(rs, shortResult.getIndex()));
                    assertNull(JdbcUtils.readIntegerNull(rs, intResult.getIndex()));
                    assertNull(JdbcUtils.readLongNull(rs, longResult.getIndex()));
                    assertNull(JdbcUtils.readFloatNull(rs, floatResult.getIndex()));
                    assertNull(JdbcUtils.readDoubleNull(rs, doubleResult.getIndex()));
                    assertNull(JdbcUtils.readBigDecimal(rs, bigDecimalResult.getIndex()));
                    assertNull(JdbcUtils.readString(rs, stringResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    @Test
    public void testRegularNullRaw() throws SQLException {
        withRegularTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(BOOLEAN_COLUMN, Parameter.ofNull(Types.BOOLEAN));
            insert.addValue(BYTE_COLUMN, Parameter.ofNull(Types.TINYINT));
            insert.addValue(SHORT_COLUMN, Parameter.ofNull(Types.SMALLINT));
            insert.addValue(INT_COLUMN, Parameter.ofNull(Types.INTEGER));
            insert.addValue(LONG_COLUMN, Parameter.ofNull(Types.BIGINT));
            insert.addValue(FLOAT_COLUMN, Parameter.ofNull(Types.REAL));
            insert.addValue(DOUBLE_COLUMN, Parameter.ofNull(Types.DOUBLE));
            insert.addValue(BIG_DECIMAL_COLUMN, Parameter.ofNull(Types.NUMERIC));
            insert.addValue(STRING_COLUMN, Parameter.ofNull(Types.VARCHAR));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn booleanResult = select.addToSelection(e.get(BOOLEAN_COLUMN));
            ResultColumn byteResult = select.addToSelection(e.get(BYTE_COLUMN));
            ResultColumn shortResult = select.addToSelection(e.get(SHORT_COLUMN));
            ResultColumn intResult = select.addToSelection(e.get(INT_COLUMN));
            ResultColumn longResult = select.addToSelection(e.get(LONG_COLUMN));
            ResultColumn floatResult = select.addToSelection(e.get(FLOAT_COLUMN));
            ResultColumn doubleResult = select.addToSelection(e.get(DOUBLE_COLUMN));
            ResultColumn bigDecimalResult = select.addToSelection(e.get(BIG_DECIMAL_COLUMN));
            ResultColumn stringResult = select.addToSelection(e.get(STRING_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertNull(JdbcUtils.readBooleanNull(rs, booleanResult.getIndex()));
                    assertNull(JdbcUtils.readByteNull(rs, byteResult.getIndex()));
                    assertNull(JdbcUtils.readShortNull(rs, shortResult.getIndex()));
                    assertNull(JdbcUtils.readIntegerNull(rs, intResult.getIndex()));
                    assertNull(JdbcUtils.readLongNull(rs, longResult.getIndex()));
                    assertNull(JdbcUtils.readFloatNull(rs, floatResult.getIndex()));
                    assertNull(JdbcUtils.readDoubleNull(rs, doubleResult.getIndex()));
                    assertNull(JdbcUtils.readBigDecimal(rs, bigDecimalResult.getIndex()));
                    assertNull(JdbcUtils.readString(rs, stringResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    @Test
    public void testRealNotNullByDefinition() throws SQLException {
        withRealTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(FLOAT_INFINITY_COLUMN, Parameter.of(Float.POSITIVE_INFINITY));
            insert.addValue(FLOAT_MINUS_INFINITY_COLUMN, Parameter.of(Float.NEGATIVE_INFINITY));
            insert.addValue(FLOAT_NAN_COLUMN, Parameter.of(Float.NaN));
            insert.addValue(DOUBLE_INFINITY_COLUMN, Parameter.of(Double.POSITIVE_INFINITY));
            insert.addValue(DOUBLE_MINUS_INFINITY_COLUMN, Parameter.of(Double.NEGATIVE_INFINITY));
            insert.addValue(DOUBLE_NAN_COLUMN, Parameter.of(Double.NaN));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn floatInfinityResult = select.addToSelection(e.get(FLOAT_INFINITY_COLUMN));
            ResultColumn floatMinusInfinityResult = select.addToSelection(e.get(FLOAT_MINUS_INFINITY_COLUMN));
            ResultColumn floatNanResult = select.addToSelection(e.get(FLOAT_NAN_COLUMN));
            ResultColumn doubleInfinityResult = select.addToSelection(e.get(DOUBLE_INFINITY_COLUMN));
            ResultColumn doubleMinusInfinityResult = select.addToSelection(e.get(DOUBLE_MINUS_INFINITY_COLUMN));
            ResultColumn doubleNanResult = select.addToSelection(e.get(DOUBLE_NAN_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertTrue(isPositiveInfinity(JdbcUtils.readFloatNotNull(rs, floatInfinityResult.getIndex())));
                    assertTrue(isNegativeInfinity(JdbcUtils.readFloatNotNull(rs, floatMinusInfinityResult.getIndex())));
                    assertTrue(Float.isNaN(JdbcUtils.readFloatNotNull(rs, floatNanResult.getIndex())));
                    assertTrue(isPositiveInfinity(JdbcUtils.readDoubleNotNull(rs, doubleInfinityResult.getIndex())));
                    assertTrue(isNegativeInfinity(JdbcUtils.readDoubleNotNull(rs, doubleMinusInfinityResult.getIndex())));
                    assertTrue(Double.isNaN(JdbcUtils.readDoubleNotNull(rs, doubleNanResult.getIndex())));
                    assertFalse(rs.next());
                }
            }
        }, true);
    }

    @Test
    public void testRealNotNullByValue() throws SQLException {
        withRealTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(FLOAT_INFINITY_COLUMN, Parameter.of(Float.POSITIVE_INFINITY));
            insert.addValue(FLOAT_MINUS_INFINITY_COLUMN, Parameter.of(Float.NEGATIVE_INFINITY));
            insert.addValue(FLOAT_NAN_COLUMN, Parameter.of(Float.NaN));
            insert.addValue(DOUBLE_INFINITY_COLUMN, Parameter.of(Double.POSITIVE_INFINITY));
            insert.addValue(DOUBLE_MINUS_INFINITY_COLUMN, Parameter.of(Double.NEGATIVE_INFINITY));
            insert.addValue(DOUBLE_NAN_COLUMN, Parameter.of(Double.NaN));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn floatInfinityResult = select.addToSelection(e.get(FLOAT_INFINITY_COLUMN));
            ResultColumn floatMinusInfinityResult = select.addToSelection(e.get(FLOAT_MINUS_INFINITY_COLUMN));
            ResultColumn floatNanResult = select.addToSelection(e.get(FLOAT_NAN_COLUMN));
            ResultColumn doubleInfinityResult = select.addToSelection(e.get(DOUBLE_INFINITY_COLUMN));
            ResultColumn doubleMinusInfinityResult = select.addToSelection(e.get(DOUBLE_MINUS_INFINITY_COLUMN));
            ResultColumn doubleNanResult = select.addToSelection(e.get(DOUBLE_NAN_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertTrue(isPositiveInfinity(JdbcUtils.readFloatNull(rs, floatInfinityResult.getIndex())));
                    assertTrue(isNegativeInfinity(JdbcUtils.readFloatNull(rs, floatMinusInfinityResult.getIndex())));
                    assertTrue(Float.isNaN(JdbcUtils.readFloatNull(rs, floatNanResult.getIndex())));
                    assertTrue(isPositiveInfinity(JdbcUtils.readDoubleNull(rs, doubleInfinityResult.getIndex())));
                    assertTrue(isNegativeInfinity(JdbcUtils.readDoubleNull(rs, doubleMinusInfinityResult.getIndex())));
                    assertTrue(Double.isNaN(JdbcUtils.readDoubleNull(rs, doubleNanResult.getIndex())));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    @Test
    public void testTimestampNotNull() throws SQLException {
        withTimestampTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(TIMESTAMP_COLUMN, Parameter.of(TIMESTAMP));
            insert.addValue(TIME_COLUMN, Parameter.of(TIME));
            insert.addValue(DATE_COLUMN, Parameter.of(DATE));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn timestampResult = select.addToSelection(e.get(TIMESTAMP_COLUMN));
            ResultColumn timeResult = select.addToSelection(e.get(TIME_COLUMN));
            ResultColumn dateResult = select.addToSelection(e.get(DATE_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals(TIMESTAMP, JdbcUtils.readTimestamp(rs, timestampResult.getIndex()));
                    assertEquals(TIME, JdbcUtils.readTime(rs, timeResult.getIndex()));
                    assertEquals(DATE, JdbcUtils.readDate(rs, dateResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    @Test
    public void testTimestampNull() throws SQLException {
        withTimestampTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(TIMESTAMP_COLUMN, Parameter.of((Timestamp) null));
            insert.addValue(TIME_COLUMN, Parameter.of((Time) null));
            insert.addValue(DATE_COLUMN, Parameter.of((Date) null));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn timestampResult = select.addToSelection(e.get(TIMESTAMP_COLUMN));
            ResultColumn timeResult = select.addToSelection(e.get(TIME_COLUMN));
            ResultColumn dateResult = select.addToSelection(e.get(DATE_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertNull(JdbcUtils.readTimestamp(rs, timestampResult.getIndex()));
                    assertNull(JdbcUtils.readTime(rs, timeResult.getIndex()));
                    assertNull(JdbcUtils.readDate(rs, dateResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    @Test
    public void testComplexNotNull() throws SQLException {
        withComplexTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(INT_ARRAY_COLUMN, Parameter.of(connection.createArrayOf("INT", INT_ARRAY)));
            insert.addValue(STRING_ARRAY_COLUMN, Parameter.of(connection.createArrayOf("TEXT", STRING_ARRAY)));
            insert.addValue(BINARY_COLUMN, Parameter.of(BINARY));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn intArrayResult = select.addToSelection(e.get(INT_ARRAY_COLUMN));
            ResultColumn stringArrayResult = select.addToSelection(e.get(STRING_ARRAY_COLUMN));
            ResultColumn binaryResult = select.addToSelection(e.get(BINARY_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertArrayEquals(INT_ARRAY, JdbcUtils.readArray(rs, intArrayResult.getIndex()));
                    assertArrayEquals(STRING_ARRAY, JdbcUtils.readArray(rs, stringArrayResult.getIndex()));
                    assertArrayEquals(BINARY, JdbcUtils.readBinary(rs, binaryResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    @Test
    public void testComplexNull() throws SQLException {
        withComplexTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(INT_ARRAY_COLUMN, Parameter.of((Array) null));
            insert.addValue(STRING_ARRAY_COLUMN, Parameter.of((Array) null));
            insert.addValue(BINARY_COLUMN, Parameter.of((byte[]) null));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn intArrayResult = select.addToSelection(e.get(INT_ARRAY_COLUMN));
            ResultColumn stringArrayResult = select.addToSelection(e.get(STRING_ARRAY_COLUMN));
            ResultColumn binaryResult = select.addToSelection(e.get(BINARY_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertNull(JdbcUtils.readArray(rs, intArrayResult.getIndex()));
                    assertNull(JdbcUtils.readArray(rs, stringArrayResult.getIndex()));
                    assertNull(JdbcUtils.readBinary(rs, binaryResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    @Test
    public void testJava8TimeNotNull() throws SQLException {
        withJava8TimeTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(INSTANT_COLUMN, Parameter.of(INSTANT));
            insert.addValue(LOCAL_DATE_COLUMN, Parameter.of(LOCAL_DATE));
            insert.addValue(LOCAL_TIME_COLUMN, Parameter.of(LOCAL_TIME));
            insert.addValue(LOCAL_DATE_TIME_COLUMN, Parameter.of(LOCAL_DATE_TIME));
            insert.addValue(ZONED_DATE_TIME_COLUMN, Parameter.of(ZONED_DATE_TIME));
            insert.addValue(OFFSET_DATE_TIME_COLUMN, Parameter.of(OFFSET_DATE_TIME));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn instantResult = select.addToSelection(e.get(INSTANT_COLUMN));
            ResultColumn localDateResult = select.addToSelection(e.get(LOCAL_DATE_COLUMN));
            ResultColumn localTimeResult = select.addToSelection(e.get(LOCAL_TIME_COLUMN));
            ResultColumn localDateTimeResult = select.addToSelection(e.get(LOCAL_DATE_TIME_COLUMN));
            ResultColumn zonedDateTimeResult = select.addToSelection(e.get(ZONED_DATE_TIME_COLUMN));
            ResultColumn offsetDateTimeResult = select.addToSelection(e.get(OFFSET_DATE_TIME_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals(INSTANT, JdbcUtils.readInstant(rs, instantResult.getIndex()));
                    assertEquals(LOCAL_DATE, JdbcUtils.readLocalDate(rs, localDateResult.getIndex()));
                    assertEquals(LOCAL_TIME, JdbcUtils.readLocalTime(rs, localTimeResult.getIndex()));
                    assertEquals(LOCAL_DATE_TIME, JdbcUtils.readLocalDateTime(rs, localDateTimeResult.getIndex()));
                    assertEquals(Instant.from(ZONED_DATE_TIME), JdbcUtils.readInstant(rs, zonedDateTimeResult.getIndex()));
                    assertEquals(Instant.from(OFFSET_DATE_TIME), JdbcUtils.readInstant(rs, offsetDateTimeResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    @Test
    public void testJava8TimeNull() throws SQLException {
        withJava8TimeTable((connection, database) -> {
            InsertQuery insert = new InsertQuery(TABLE);
            insert.addValue(INSTANT_COLUMN, Parameter.of((Instant) null));
            insert.addValue(LOCAL_DATE_COLUMN, Parameter.of((LocalDate) null));
            insert.addValue(LOCAL_TIME_COLUMN, Parameter.of((LocalTime) null));
            insert.addValue(LOCAL_DATE_TIME_COLUMN, Parameter.of((LocalDateTime) null));
            insert.addValue(ZONED_DATE_TIME_COLUMN, Parameter.of((ZonedDateTime) null));
            insert.addValue(OFFSET_DATE_TIME_COLUMN, Parameter.of((OffsetDateTime) null));
            insert.toStatement(new JdbcStatementCompiler(connection)).executeUpdate();

            TableReference e = TABLE.refer();
            SelectQuery select = new SelectQuery();
            ResultColumn instantResult = select.addToSelection(e.get(INSTANT_COLUMN));
            ResultColumn localDateResult = select.addToSelection(e.get(LOCAL_DATE_COLUMN));
            ResultColumn localTimeResult = select.addToSelection(e.get(LOCAL_TIME_COLUMN));
            ResultColumn localDateTimeResult = select.addToSelection(e.get(LOCAL_DATE_TIME_COLUMN));
            ResultColumn zonedDateTimeResult = select.addToSelection(e.get(ZONED_DATE_TIME_COLUMN));
            ResultColumn offsetDateTimeResult = select.addToSelection(e.get(OFFSET_DATE_TIME_COLUMN));

            try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                try (ResultSet rs = statement.executeQuery()) {
                    assertTrue(rs.next());
                    assertNull(JdbcUtils.readInstant(rs, instantResult.getIndex()));
                    assertNull(JdbcUtils.readLocalDate(rs, localDateResult.getIndex()));
                    assertNull(JdbcUtils.readLocalTime(rs, localTimeResult.getIndex()));
                    assertNull(JdbcUtils.readLocalDateTime(rs, localDateTimeResult.getIndex()));
                    assertNull(JdbcUtils.readInstant(rs, zonedDateTimeResult.getIndex()));
                    assertNull(JdbcUtils.readInstant(rs, offsetDateTimeResult.getIndex()));
                    assertFalse(rs.next());
                }
            }
        }, false);
    }

    private static void withRegularTable(Consumer consumer, boolean notNull) throws SQLException {
        withDatabase((connection, database) -> withTable(
                connection, database, TABLE.getName(),
                new TestDatabaseColumn[]{
                        new TestDatabaseColumn(BOOLEAN_COLUMN.getName(), "BOOLEAN", notNull, null),
                        new TestDatabaseColumn(BYTE_COLUMN.getName(), "SMALLINT", notNull, null), // No TINYINT in PostgreSQL
                        new TestDatabaseColumn(SHORT_COLUMN.getName(), "SMALLINT", notNull, null),
                        new TestDatabaseColumn(INT_COLUMN.getName(), "INT", notNull, null),
                        new TestDatabaseColumn(LONG_COLUMN.getName(), "BIGINT", notNull, null),
                        new TestDatabaseColumn(FLOAT_COLUMN.getName(), "REAL", notNull, null),
                        new TestDatabaseColumn(DOUBLE_COLUMN.getName(), "DOUBLE PRECISION", notNull, null), // No DOUBLE in PostgreSQL
                        new TestDatabaseColumn(BIG_DECIMAL_COLUMN.getName(), "NUMERIC", notNull, null),
                        new TestDatabaseColumn(STRING_COLUMN.getName(), "TEXT", notNull, null)
                },
                () -> {
                    consumer.accept(connection, database);
                    return null;
                }));
    }

    private static void withRealTable(Consumer consumer, boolean notNull) throws SQLException {
        withDatabase((connection, database) -> withTable(
                connection, database, TABLE.getName(),
                new TestDatabaseColumn[]{
                        new TestDatabaseColumn(FLOAT_INFINITY_COLUMN.getName(), "REAL", notNull, null),
                        new TestDatabaseColumn(FLOAT_MINUS_INFINITY_COLUMN.getName(), "REAL", notNull, null),
                        new TestDatabaseColumn(FLOAT_NAN_COLUMN.getName(), "REAL", notNull, null),
                        new TestDatabaseColumn(DOUBLE_INFINITY_COLUMN.getName(), "DOUBLE PRECISION", notNull, null), // No DOUBLE in PostgreSQL
                        new TestDatabaseColumn(DOUBLE_MINUS_INFINITY_COLUMN.getName(), "DOUBLE PRECISION", notNull, null),
                        new TestDatabaseColumn(DOUBLE_NAN_COLUMN.getName(), "DOUBLE PRECISION", notNull, null)
                },
                () -> {
                    consumer.accept(connection, database);
                    return null;
                }));
    }

    private static void withTimestampTable(Consumer consumer, boolean notNull) throws SQLException {
        withDatabase((connection, database) -> withTable(
                connection, database, TABLE.getName(),
                new TestDatabaseColumn[]{
                        new TestDatabaseColumn(TIMESTAMP_COLUMN.getName(), "TIMESTAMP", notNull, null),
                        new TestDatabaseColumn(TIME_COLUMN.getName(), "TIME", notNull, null),
                        new TestDatabaseColumn(DATE_COLUMN.getName(), "DATE", notNull, null)
                },
                () -> {
                    consumer.accept(connection, database);
                    return null;
                }));
    }

    private static void withComplexTable(Consumer consumer, boolean notNull) throws SQLException {
        withDatabase((connection, database) -> withTable(
                connection, database, TABLE.getName(),
                new TestDatabaseColumn[]{
                        new TestDatabaseColumn(INT_ARRAY_COLUMN.getName(), "INT[]", notNull, null),
                        new TestDatabaseColumn(STRING_ARRAY_COLUMN.getName(), "TEXT[]", notNull, null),
                        new TestDatabaseColumn(BINARY_COLUMN.getName(), "BYTEA", notNull, null)
                },
                () -> {
                    consumer.accept(connection, database);
                    return null;
                }));
    }

    private static void withJava8TimeTable(Consumer consumer, boolean notNull) throws SQLException {
        withDatabase((connection, database) -> withTable(
                connection, database, TABLE.getName(),
                new TestDatabaseColumn[]{
                        new TestDatabaseColumn(INSTANT_COLUMN.getName(), "TIMESTAMP WITH TIME ZONE", notNull, null),
                        new TestDatabaseColumn(LOCAL_DATE_COLUMN.getName(), "DATE", notNull, null),
                        new TestDatabaseColumn(LOCAL_TIME_COLUMN.getName(), "TIME(6)", notNull, null),
                        new TestDatabaseColumn(LOCAL_DATE_TIME_COLUMN.getName(), "TIMESTAMP", notNull, null),
                        new TestDatabaseColumn(ZONED_DATE_TIME_COLUMN.getName(), "TIMESTAMP WITH TIME ZONE", notNull, null),
                        new TestDatabaseColumn(OFFSET_DATE_TIME_COLUMN.getName(), "TIMESTAMP WITH TIME ZONE", notNull, null)
                },
                () -> {
                    consumer.accept(connection, database);
                    return null;
                }));
    }

    private static boolean isPositiveInfinity(float value) {
        return Float.isInfinite(value) && value > 0;
    }

    private static boolean isPositiveInfinity(double value) {
        return Double.isInfinite(value) && value > 0;
    }

    private static boolean isNegativeInfinity(float value) {
        return Float.isInfinite(value) && value < 0;
    }

    private static boolean isNegativeInfinity(double value) {
        return Double.isInfinite(value) && value < 0;
    }
}
