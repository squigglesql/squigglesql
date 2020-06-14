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
package com.github.squigglesql.squigglesql.join.multiple;

import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.TestUtils;
import com.github.squigglesql.squigglesql.criteria.MatchCriteria;
import com.github.squigglesql.squigglesql.databases.TestDatabase;
import com.github.squigglesql.squigglesql.databases.TestDatabaseColumn;
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

import static com.github.squigglesql.squigglesql.TestUtils.*;
import static org.junit.Assert.assertEquals;

// Examples are taken from https://learnsql.com/blog/illustrated-guide-multiple-join/
public class MultipleJoinTest {

    private static final TestDatabaseColumn[] PERSON_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn("last_name", "TEXT", true, null)
    };

    private static final TestDatabaseColumn[] COLOR_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn("name", "TEXT", true, null)
    };

    private static final TestDatabaseColumn[] VEHICLE_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn("name", "TEXT", true, null),
            new TestDatabaseColumn("color_id", "INTEGER", false, null),
            new TestDatabaseColumn("person_id", "INTEGER", false, null)
    };

    private static final Person WATSON = new Person(1, "Watson");
    private static final Person MILLER = new Person(2, "Miller");
    private static final Person SMITH = new Person(3, "Smith");
    private static final Person BROWN = new Person(4, "Brown");

    private static final Color GREEN = new Color(1, "green");
    private static final Color YELLOW = new Color(2, "yellow");
    private static final Color BLUE = new Color(3, "blue");

    private static final Vehicle CAR = new Vehicle(1, "car", 1, 4);
    private static final Vehicle BICYCLE = new Vehicle(2, "bicycle", 2, null);
    private static final Vehicle MOTORCYCLE = new Vehicle(3, "motorcycle", null, 1);
    private static final Vehicle SCOOTER = new Vehicle(4, "scooter", 1, 3);

    private static final Person[] PERSONS = new Person[]{WATSON, MILLER, SMITH, BROWN};
    private static final Color[] COLORS = new Color[]{GREEN, YELLOW, BLUE};
    private static final Vehicle[] VEHICLES = new Vehicle[]{CAR, BICYCLE, MOTORCYCLE, SCOOTER};

    @Test
    public void testDoubleInner() throws SQLException {
        SelectQuery select = new SelectQuery();
        RecordMapper mapper = new RecordMapper(select);
        TableReference v = mapper.getVehicleRef();
        TableReference c = mapper.getColorRef();
        TableReference p = mapper.getPersonRef();
        FromItem join1 = new QualifiedJoin(v, QualifiedJoinKind.INNER, c,
                new MatchCriteria(v.get(VehicleDao.COLOR_ID), MatchCriteria.EQUALS, c.get(ColorDao.ID)));
        FromItem join2 = new QualifiedJoin(join1, QualifiedJoinKind.INNER, p,
                new MatchCriteria(v.get(VehicleDao.PERSON_ID), MatchCriteria.EQUALS, p.get(PersonDao.ID)));
        select.addFrom(join2);

        assertEquals("SELECT\n"
                + "    v.name,\n"
                + "    c.name,\n"
                + "    p.last_name\n"
                + "FROM\n"
                + "    vehicle v\n"
                + "    INNER JOIN color c ON v.color_id = c.id\n"
                + "    INNER JOIN person p ON v.person_id = p.id", select.toString());

        Record[] expectedRecords = new Record[]{
                new Record("car", "green", "Brown"),
                new Record("scooter", "green", "Smith")
        };

        withContents((connection, database) -> {
            List<Record> records = JdbcUtils.selectAll(select, connection, mapper);
            assertEquals(new HashSet<>(Arrays.asList(expectedRecords)), new HashSet<>(records));
        });
    }

    @Test
    public void testLeftInner() throws SQLException {
        SelectQuery select = new SelectQuery();
        RecordMapper mapper = new RecordMapper(select);
        TableReference v = mapper.getVehicleRef();
        TableReference c = mapper.getColorRef();
        TableReference p = mapper.getPersonRef();
        FromItem join1 = new QualifiedJoin(p, QualifiedJoinKind.LEFT, v,
                new MatchCriteria(v.get(VehicleDao.PERSON_ID), MatchCriteria.EQUALS, p.get(PersonDao.ID)));
        FromItem join2 = new QualifiedJoin(join1, QualifiedJoinKind.INNER, c,
                new MatchCriteria(v.get(VehicleDao.COLOR_ID), MatchCriteria.EQUALS, c.get(ColorDao.ID)));
        select.addFrom(join2);

        assertEquals("SELECT\n"
                + "    v.name,\n"
                + "    c.name,\n"
                + "    p.last_name\n"
                + "FROM\n"
                + "    person p\n"
                + "    LEFT JOIN vehicle v ON v.person_id = p.id\n"
                + "    INNER JOIN color c ON v.color_id = c.id", select.toString());

        Record[] expectedRecords = new Record[]{
                new Record("car", "green", "Brown"),
                new Record("scooter", "green", "Smith")
        };

        withContents((connection, database) -> {
            List<Record> records = JdbcUtils.selectAll(select, connection, mapper);
            assertEquals(new HashSet<>(Arrays.asList(expectedRecords)), new HashSet<>(records));
        });
    }

    @Test
    public void testInnerRight() throws SQLException {
        SelectQuery select = new SelectQuery();
        RecordMapper mapper = new RecordMapper(select);
        TableReference v = mapper.getVehicleRef();
        TableReference c = mapper.getColorRef();
        TableReference p = mapper.getPersonRef();
        FromItem join1 = new QualifiedJoin(v, QualifiedJoinKind.INNER, c,
                new MatchCriteria(v.get(VehicleDao.COLOR_ID), MatchCriteria.EQUALS, c.get(ColorDao.ID)));
        FromItem join2 = new QualifiedJoin(join1, QualifiedJoinKind.RIGHT, p,
                new MatchCriteria(v.get(VehicleDao.PERSON_ID), MatchCriteria.EQUALS, p.get(PersonDao.ID)));
        select.addFrom(join2);

        assertEquals("SELECT\n"
                + "    v.name,\n"
                + "    c.name,\n"
                + "    p.last_name\n"
                + "FROM\n"
                + "    vehicle v\n"
                + "    INNER JOIN color c ON v.color_id = c.id\n"
                + "    RIGHT JOIN person p ON v.person_id = p.id", select.toString());

        Record[] expectedRecords = new Record[]{
                new Record("car", "green", "Brown"),
                new Record("scooter", "green", "Smith"),
                new Record(null, null, "Miller"),
                new Record(null, null, "Watson")
        };

        withContents((connection, database) -> {
            List<Record> records = JdbcUtils.selectAll(select, connection, mapper);
            assertEquals(new HashSet<>(Arrays.asList(expectedRecords)), new HashSet<>(records));
        });
    }

    @Test
    public void testDoubleFull() throws SQLException {
        SelectQuery select = new SelectQuery();
        RecordMapper mapper = new RecordMapper(select);
        TableReference v = mapper.getVehicleRef();
        TableReference c = mapper.getColorRef();
        TableReference p = mapper.getPersonRef();
        FromItem join1 = new QualifiedJoin(v, QualifiedJoinKind.FULL, c,
                new MatchCriteria(v.get(VehicleDao.COLOR_ID), MatchCriteria.EQUALS, c.get(ColorDao.ID)));
        FromItem join2 = new QualifiedJoin(join1, QualifiedJoinKind.FULL, p,
                new MatchCriteria(v.get(VehicleDao.PERSON_ID), MatchCriteria.EQUALS, p.get(PersonDao.ID)));
        select.addFrom(join2);

        assertEquals("SELECT\n"
                + "    v.name,\n"
                + "    c.name,\n"
                + "    p.last_name\n"
                + "FROM\n"
                + "    vehicle v\n"
                + "    FULL JOIN color c ON v.color_id = c.id\n"
                + "    FULL JOIN person p ON v.person_id = p.id", select.toString());

        Record[] expectedRecords = new Record[]{
                new Record("car", "green", "Brown"),
                new Record("scooter", "green", "Smith"),
                new Record(null, null, "Miller"),
                new Record("motorcycle", null, "Watson"),
                new Record("bicycle", "yellow", null),
                new Record(null, "blue", null)
        };

        withContents((connection, database) -> {
            if (!database.supportsFullJoin()) {
                return;
            }
            List<Record> records = JdbcUtils.selectAll(select, connection, mapper);
            assertEquals(new HashSet<>(Arrays.asList(expectedRecords)), new HashSet<>(records));
        });
    }

    @Test
    public void testInnerFull() throws SQLException {
        SelectQuery select = new SelectQuery();
        RecordMapper mapper = new RecordMapper(select);
        TableReference v = mapper.getVehicleRef();
        TableReference c = mapper.getColorRef();
        TableReference p = mapper.getPersonRef();
        FromItem join1 = new QualifiedJoin(v, QualifiedJoinKind.INNER, c,
                new MatchCriteria(v.get(VehicleDao.COLOR_ID), MatchCriteria.EQUALS, c.get(ColorDao.ID)));
        FromItem join2 = new QualifiedJoin(join1, QualifiedJoinKind.FULL, p,
                new MatchCriteria(v.get(VehicleDao.PERSON_ID), MatchCriteria.EQUALS, p.get(PersonDao.ID)));
        select.addFrom(join2);

        assertEquals("SELECT\n"
                + "    v.name,\n"
                + "    c.name,\n"
                + "    p.last_name\n"
                + "FROM\n"
                + "    vehicle v\n"
                + "    INNER JOIN color c ON v.color_id = c.id\n"
                + "    FULL JOIN person p ON v.person_id = p.id", select.toString());

        Record[] expectedRecords = new Record[]{
                new Record("car", "green", "Brown"),
                new Record("scooter", "green", "Smith"),
                new Record(null, null, "Miller"),
                new Record(null, null, "Watson"),
                new Record("bicycle", "yellow", null)
        };

        withContents((connection, database) -> {
            if (!database.supportsFullJoin()) {
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

    private static <T> void withTables(Connection connection, TestDatabase database, TestUtils.Supplier<T> supplier) throws SQLException {
        database.dropTable(connection, VehicleDao.TABLE.getName());
        database.dropTable(connection, ColorDao.TABLE.getName());
        database.dropTable(connection, PersonDao.TABLE.getName());
        withTable(connection, database, PersonDao.TABLE.getName(), PERSON_COLUMNS,
                () -> withTable(connection, database, ColorDao.TABLE.getName(), COLOR_COLUMNS,
                        () -> withTable(connection, database, VehicleDao.TABLE.getName(), VEHICLE_COLUMNS, supplier)));
    }

    private static <T> T withRecords(Connection connection, TestUtils.Supplier<T> supplier) throws SQLException {
        for (Person person : PERSONS) {
            PersonDao.insert(connection, person);
        }
        for (Color color : COLORS) {
            ColorDao.insert(connection, color);
        }
        for (Vehicle vehicle : VEHICLES) {
            VehicleDao.insert(connection, vehicle);
        }
        return supplier.get();
    }
}
