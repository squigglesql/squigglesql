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
package io.zatarox.squiggle;

import io.zatarox.squiggle.parameter.Parameter;
import io.zatarox.squiggle.query.InsertQuery;
import io.zatarox.squiggle.query.ResultColumn;
import io.zatarox.squiggle.query.SelectQuery;
import io.zatarox.squiggle.statement.JdbcStatementCompiler;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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

    private static final TableColumn TIMESTAMP_COLUMN = TABLE.get("timestamp_c");
    private static final TableColumn TIME_COLUMN = TABLE.get("time_c");
    private static final TableColumn DATE_COLUMN = TABLE.get("date_c");

    private static final TableColumn INT_ARRAY_COLUMN = TABLE.get("int_array_c");
    private static final TableColumn STRING_ARRAY_COLUMN = TABLE.get("string_array_c");
    private static final TableColumn BINARY_COLUMN = TABLE.get("binary_c");

    @Test
    public void testRegularNotNull() throws SQLException {
        withRegularTable(new TestUtils.Mapper<Void>() {
            @Override
            public Void apply(Connection connection) throws SQLException {
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
                ResultSet rs = select.toStatement(new JdbcStatementCompiler(connection)).executeQuery();

                assertEquals(true, rs.next());
                assertEquals(BOOLEAN, rs.getBoolean(booleanResult.getIndex()));
                assertEquals(BYTE, rs.getByte(byteResult.getIndex()));
                assertEquals(SHORT, rs.getShort(shortResult.getIndex()));
                assertEquals(INT, rs.getInt(intResult.getIndex()));
                assertEquals(LONG, rs.getLong(longResult.getIndex()));
                assertEquals(FLOAT, rs.getFloat(floatResult.getIndex()), 0.0f);
                assertEquals(DOUBLE, rs.getDouble(doubleResult.getIndex()), 0.0d);
                assertEquals(BIG_DECIMAL, rs.getBigDecimal(bigDecimalResult.getIndex()));
                assertEquals(STRING, rs.getString(stringResult.getIndex()));
                assertEquals(false, rs.next());

                return null;
            }
        });
    }

    @Test
    public void testRegularNull() throws SQLException {
        withRegularTable(new TestUtils.Mapper<Void>() {
            @Override
            public Void apply(Connection connection) throws SQLException {
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
                ResultSet rs = select.toStatement(new JdbcStatementCompiler(connection)).executeQuery();

                assertEquals(true, rs.next());
                assertEquals(null, rs.getObject(booleanResult.getIndex()));
                assertEquals(null, rs.getObject(byteResult.getIndex()));
                assertEquals(null, rs.getObject(shortResult.getIndex()));
                assertEquals(null, rs.getObject(intResult.getIndex()));
                assertEquals(null, rs.getObject(longResult.getIndex()));
                assertEquals(null, rs.getObject(floatResult.getIndex()));
                assertEquals(null, rs.getObject(doubleResult.getIndex()));
                assertEquals(null, rs.getBigDecimal(bigDecimalResult.getIndex()));
                assertEquals(null, rs.getString(stringResult.getIndex()));
                assertEquals(false, rs.next());

                return null;
            }
        });
    }

    @Test
    public void testTimestampNotNull() throws SQLException {
        withTimestampTable(new TestUtils.Mapper<Void>() {
            @Override
            public Void apply(Connection connection) throws SQLException {
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
                ResultSet rs = select.toStatement(new JdbcStatementCompiler(connection)).executeQuery();

                assertEquals(true, rs.next());
                assertEquals(TIMESTAMP, rs.getTimestamp(timestampResult.getIndex()));
                assertEquals(TIME, rs.getTime(timeResult.getIndex()));
                assertEquals(DATE, rs.getDate(dateResult.getIndex()));
                assertEquals(false, rs.next());

                return null;
            }
        });
    }

    @Test
    public void testTimestampNull() throws SQLException {
        withTimestampTable(new TestUtils.Mapper<Void>() {
            @Override
            public Void apply(Connection connection) throws SQLException {
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
                ResultSet rs = select.toStatement(new JdbcStatementCompiler(connection)).executeQuery();

                assertEquals(true, rs.next());
                assertEquals(null, rs.getTimestamp(timestampResult.getIndex()));
                assertEquals(null, rs.getTime(timeResult.getIndex()));
                assertEquals(null, rs.getDate(dateResult.getIndex()));
                assertEquals(false, rs.next());

                return null;
            }
        });
    }

    @Test
    public void testComplexNotNull() throws SQLException {
        withComplexTable(new TestUtils.Mapper<Void>() {
            @Override
            public Void apply(Connection connection) throws SQLException {
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
                ResultSet rs = select.toStatement(new JdbcStatementCompiler(connection)).executeQuery();

                assertEquals(true, rs.next());
                assertArrayEquals(INT_ARRAY, (Integer[]) rs.getArray(intArrayResult.getIndex()).getArray());
                assertArrayEquals(STRING_ARRAY, (String[]) rs.getArray(stringArrayResult.getIndex()).getArray());
                assertArrayEquals(BINARY, rs.getBytes(binaryResult.getIndex()));
                assertEquals(false, rs.next());

                return null;
            }
        });
    }

    @Test
    public void testComplexNull() throws SQLException {
        withComplexTable(new TestUtils.Mapper<Void>() {
            @Override
            public Void apply(Connection connection) throws SQLException {
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
                ResultSet rs = select.toStatement(new JdbcStatementCompiler(connection)).executeQuery();

                assertEquals(true, rs.next());
                assertEquals(null, rs.getArray(intArrayResult.getIndex()));
                assertEquals(null, rs.getArray(stringArrayResult.getIndex()));
                assertEquals(null, rs.getBytes(binaryResult.getIndex()));
                assertEquals(false, rs.next());

                return null;
            }
        });
    }

    private static <T> T withRegularTable(final TestUtils.Mapper<T> mapper) throws SQLException {
        return withTable(mapper, "CREATE TABLE " + TABLE.getName() + " ("
                + BOOLEAN_COLUMN.getName() + " BOOLEAN,"
                + BYTE_COLUMN.getName() + " SMALLINT," // No TINYINT in PostgreSQL
                + SHORT_COLUMN.getName() + " SMALLINT,"
                + INT_COLUMN.getName() + " INT,"
                + LONG_COLUMN.getName() + " BIGINT,"
                + FLOAT_COLUMN.getName() + " REAL,"
                + DOUBLE_COLUMN.getName() + " DOUBLE PRECISION," // No DOUBLE in PostgreSQL
                + BIG_DECIMAL_COLUMN.getName() + " NUMERIC,"
                + STRING_COLUMN.getName() + " TEXT"
                + ")");
    }

    private static <T> T withTimestampTable(final TestUtils.Mapper<T> mapper) throws SQLException {
        return withTable(mapper, "CREATE TABLE " + TABLE.getName() + " ("
                + TIMESTAMP_COLUMN.getName() + " TIMESTAMP,"
                + TIME_COLUMN.getName() + " TIME,"
                + DATE_COLUMN.getName() + " DATE"
                + ")");
    }

    private static <T> T withComplexTable(final TestUtils.Mapper<T> mapper) throws SQLException {
        return withTable(mapper, "CREATE TABLE " + TABLE.getName() + " ("
                + INT_ARRAY_COLUMN.getName() + " INT[],"
                + STRING_ARRAY_COLUMN.getName() + " TEXT[],"
                + BINARY_COLUMN.getName() + " BYTEA"
                + ")");
    }

    private static <T> T withTable(final TestUtils.Mapper<T> mapper, final String createQuery) throws SQLException {
        return TestUtils.withDatabase(new TestUtils.Mapper<T>() {
            @Override
            public T apply(Connection connection) throws SQLException {
                Statement dropStatement = connection.createStatement();
                dropStatement.execute("DROP TABLE IF EXISTS " + TABLE.getName());

                Statement createStatement = connection.createStatement();
                createStatement.execute(createQuery);

                T result = mapper.apply(connection);

                // We drop the table only if the test succeeds. Otherwise we preserve it for debugging purposes.
                try {
                    Statement statement = connection.createStatement();
                    statement.execute("DROP TABLE IF EXISTS " + TABLE.getName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return result;
            }
        });
    }
}
