/*
 * Copyright 2019-2020 Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.join.single;

import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.criteria.MatchCriteria;
import com.github.squigglesql.squigglesql.databases.TestDatabase;
import com.github.squigglesql.squigglesql.databases.TestDatabaseColumn;
import com.github.squigglesql.squigglesql.join.CrossJoin;
import com.github.squigglesql.squigglesql.join.QualifiedJoin;
import com.github.squigglesql.squigglesql.join.QualifiedJoinKind;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.squigglesql.squigglesql.TestUtils.*;
import static org.junit.Assert.assertEquals;

// Examples are taken from https://learnsql.com/blog/illustrated-guide-multiple-join/
public class SingleJoinTest {

    private static final TestDatabaseColumn[] TSHIRT_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn(TshirtDao.SIZE.getName(), "TEXT", false, null),
            new TestDatabaseColumn(TshirtDao.COLOR_ID.getName(), "INTEGER", false, null) // no reference because nullable
    };

    private static final TestDatabaseColumn[] COLOR_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn(ColorDao.COLOR.getName(), "TEXT", false, null)
    };

    private static final Color YELLOW = new Color(1, "yellow");
    private static final Color NO_COLOR = new Color(2, null);
    private static final Color BLUE = new Color(3, "blue");

    private static final Tshirt TSHIRT_1 = new Tshirt(1, "S", null);
    private static final Tshirt TSHIRT_2 = new Tshirt(2, "M", YELLOW.getId());
    private static final Tshirt TSHIRT_3 = new Tshirt(3, null, BLUE.getId());

    private static final Color[] COLORS = new Color[]{YELLOW, NO_COLOR, BLUE};
    private static final Tshirt[] TSHIRTS = new Tshirt[]{TSHIRT_1, TSHIRT_2, TSHIRT_3};

    @Test
    public void testCrossJoin() throws SQLException {
        testFor(CrossJoin::new, "tshirt t\n    CROSS JOIN color c", database -> true, new Record[]{
                new Record(TSHIRT_1, YELLOW),
                new Record(TSHIRT_2, YELLOW),
                new Record(TSHIRT_3, YELLOW),
                new Record(TSHIRT_1, NO_COLOR),
                new Record(TSHIRT_2, NO_COLOR),
                new Record(TSHIRT_3, NO_COLOR),
                new Record(TSHIRT_1, BLUE),
                new Record(TSHIRT_2, BLUE),
                new Record(TSHIRT_3, BLUE)
        });
    }

    @Test
    public void testInnerJoin() throws SQLException {
        testFor(QualifiedJoinKind.INNER, database -> true, new Record[]{
                new Record(TSHIRT_2, YELLOW),
                new Record(TSHIRT_3, BLUE)
        });
    }

    @Test
    public void testLeftJoin() throws SQLException {
        testFor(QualifiedJoinKind.LEFT, database -> true, new Record[]{
                new Record(TSHIRT_1, null),
                new Record(TSHIRT_2, YELLOW),
                new Record(TSHIRT_3, BLUE)
        });
    }

    @Test
    public void testRightJoin() throws SQLException {
        testFor(QualifiedJoinKind.RIGHT, database -> true, new Record[]{
                new Record(TSHIRT_2, YELLOW),
                new Record(TSHIRT_3, BLUE),
                new Record(null, NO_COLOR)
        });
    }

    @Test
    public void testFullJoin() throws SQLException {
        testFor(QualifiedJoinKind.FULL, TestDatabase::supportsFullJoin, new Record[]{
                new Record(TSHIRT_1, null),
                new Record(TSHIRT_2, YELLOW),
                new Record(TSHIRT_3, BLUE),
                new Record(null, NO_COLOR)
        });
    }

    private void testFor(QualifiedJoinKind joinKind, Function<TestDatabase, Boolean> supports,
                         Record[] expectedRecords) throws SQLException {
        testFor((t, c) -> new QualifiedJoin(t, joinKind, c,
                        new MatchCriteria(t.get(TshirtDao.COLOR_ID), MatchCriteria.EQUALS, c.get(ColorDao.ID))),
                "tshirt t\n    " + joinKind.name() + " JOIN color c ON t.color_id = c.id", supports, expectedRecords);
    }

    private void testFor(BiFunction<TableReference, TableReference, FromItem> fromItem, String fromSql,
                         Function<TestDatabase, Boolean> supports, Record[] expectedRecords) throws SQLException {
        SelectQuery select = new SelectQuery();
        RecordMapper mapper = new RecordMapper(select);
        select.addFrom(fromItem.apply(mapper.getTshirtRef(), mapper.getColorRef()));

        assertEquals("SELECT\n"
                + "    t.id,\n"
                + "    t.size,\n"
                + "    t.color_id,\n"
                + "    c.id,\n"
                + "    c.color\n"
                + "FROM\n"
                + "    " + fromSql, select.toString());

        withContents((connection, database) -> {
            if (!supports.apply(database)) {
                return;
            }
            List<Record> records = JdbcUtils.selectAll(select, connection, mapper);
            assertEquals(new HashSet<>(Arrays.asList(expectedRecords)), new HashSet<>(records));
        });
    }

    private static void withContents(Consumer consumer) throws SQLException {
        withDatabase(
                (connection, database) -> withTables(connection, database,
                        () -> withRecords(connection, () -> {
                            consumer.accept(connection, database);
                            return null;
                        })));
    }

    private static <T> void withTables(Connection connection, TestDatabase database, Supplier<T> supplier) throws SQLException {
        database.dropTable(connection, TshirtDao.TABLE.getName());
        database.dropTable(connection, ColorDao.TABLE.getName());
        withTable(connection, database, ColorDao.TABLE.getName(), COLOR_COLUMNS,
                () -> withTable(connection, database, TshirtDao.TABLE.getName(), TSHIRT_COLUMNS, supplier));
    }

    private static <T> T withRecords(Connection connection, Supplier<T> supplier) throws SQLException {
        for (Color color : COLORS) {
            ColorDao.insert(connection, color);
        }
        for (Tshirt tshirt : TSHIRTS) {
            TshirtDao.insert(connection, tshirt);
        }
        return supplier.get();
    }
}
