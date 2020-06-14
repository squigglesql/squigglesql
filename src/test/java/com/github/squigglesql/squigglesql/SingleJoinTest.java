package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.criteria.MatchCriteria;
import com.github.squigglesql.squigglesql.databases.TestDatabase;
import com.github.squigglesql.squigglesql.databases.TestDatabaseColumn;
import com.github.squigglesql.squigglesql.join.CrossJoin;
import com.github.squigglesql.squigglesql.join.QualifiedJoin;
import com.github.squigglesql.squigglesql.join.QualifiedJoinKind;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
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

    private static final Table TSHIRT = new Table("tshirt");
    private static final TableColumn TSHIRT_ID = TSHIRT.get("id");
    private static final TableColumn TSHIRT_SIZE = TSHIRT.get("size");
    private static final TableColumn TSHIRT_COLOR_ID = TSHIRT.get("color_id");

    private static final Table COLOR = new Table("color");
    private static final TableColumn COLOR_ID = COLOR.get("id");
    private static final TableColumn COLOR_COLOR = COLOR.get("color");

    private static final TestDatabaseColumn[] TSHIRT_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn(TSHIRT_SIZE.getName(), "TEXT", false, null),
            new TestDatabaseColumn(TSHIRT_COLOR_ID.getName(), "INTEGER", false, null) // no reference because nullable
    };

    private static final TestDatabaseColumn[] COLOR_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn(COLOR_COLOR.getName(), "TEXT", false, null)
    };

    private static final Color YELLOW = new Color(1, "yellow");
    private static final Color NO_COLOR = new Color(2, null);
    private static final Color BLUE = new Color(3, "blue");

    private static final Tshirt TSHIRT_1 = new Tshirt(1, "S", null);
    private static final Tshirt TSHIRT_2 = new Tshirt(2, "M", YELLOW.id);
    private static final Tshirt TSHIRT_3 = new Tshirt(3, null, BLUE.id);

    private static final Color[] COLORS = new Color[]{YELLOW, NO_COLOR, BLUE};
    private static final Tshirt[] TSHIRTS = new Tshirt[]{TSHIRT_1, TSHIRT_2, TSHIRT_3};

    @Test
    public void testCrossJoin() throws SQLException {
        testFor(CrossJoin::new, "tshirt t CROSS JOIN color c", database -> true, new Record[]{
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
                        new MatchCriteria(t.get(TSHIRT_COLOR_ID), MatchCriteria.EQUALS, c.get(COLOR_ID))),
                "tshirt t " + joinKind.name() + " JOIN color c ON t.color_id = c.id", supports, expectedRecords);
    }

    private void testFor(BiFunction<TableReference, TableReference, FromItem> fromItem, String fromSql,
                         Function<TestDatabase, Boolean> supports, Record[] expectedRecords) throws SQLException {
        SelectQuery select = new SelectQuery();
        RecordMapper mapper = new RecordMapper(select);
        select.addFrom(fromItem.apply(mapper.tshirtMapper.ref, mapper.colorMapper.ref));

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

    private static <T> T withTables(Connection connection, TestDatabase database, Supplier<T> supplier) throws SQLException {
        database.dropTable(connection, TSHIRT.getName());
        database.dropTable(connection, COLOR.getName());
        return withTable(connection, database, COLOR.getName(), COLOR_COLUMNS,
                () -> withTable(connection, database, TSHIRT.getName(), TSHIRT_COLUMNS, supplier));
    }

    private static <T> T withRecords(Connection connection, Supplier<T> supplier) throws SQLException {
        for (Color color : COLORS) {
            InsertQuery query = new InsertQuery(COLOR);
            query.addValue(COLOR_ID, Parameter.of(color.id));
            query.addValue(COLOR_COLOR, Parameter.of(color.color));
            JdbcUtils.insert(query, connection, rs -> rs.getInt(1));
        }
        for (Tshirt tshirt : TSHIRTS) {
            InsertQuery query = new InsertQuery(TSHIRT);
            query.addValue(TSHIRT_ID, Parameter.of(tshirt.id));
            query.addValue(TSHIRT_SIZE, Parameter.of(tshirt.size));
            query.addValue(TSHIRT_COLOR_ID, Parameter.of(tshirt.colorId));
            JdbcUtils.insert(query, connection, rs -> rs.getInt(1));
        }
        return supplier.get();
    }

    private static class Tshirt {

        private final int id;
        private final String size;
        private final Integer colorId;

        private Tshirt(int id, String size, Integer colorId) {
            this.id = id;
            this.size = size;
            this.colorId = colorId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Tshirt tshirt = (Tshirt) o;

            if (id != tshirt.id) return false;
            if (size != null ? !size.equals(tshirt.size) : tshirt.size != null) return false;
            return colorId != null ? colorId.equals(tshirt.colorId) : tshirt.colorId == null;
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + (size != null ? size.hashCode() : 0);
            result = 31 * result + (colorId != null ? colorId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Tshirt{" +
                    "id=" + id +
                    ", size='" + size + '\'' +
                    ", colorId=" + colorId +
                    '}';
        }
    }

    private static class Color {

        private final int id;
        private final String color;

        private Color(int id, String color) {
            this.id = id;
            this.color = color;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Color color1 = (Color) o;

            if (id != color1.id) return false;
            return color != null ? color.equals(color1.color) : color1.color == null;
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + (color != null ? color.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Color{" +
                    "id=" + id +
                    ", color='" + color + '\'' +
                    '}';
        }
    }

    private static class Record {

        private final Tshirt tshirt;
        private final Color color;

        private Record(Tshirt tshirt, Color color) {
            this.tshirt = tshirt;
            this.color = color;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Record record = (Record) o;

            if (tshirt != null ? !tshirt.equals(record.tshirt) : record.tshirt != null) return false;
            return color != null ? color.equals(record.color) : record.color == null;
        }

        @Override
        public int hashCode() {
            int result = tshirt != null ? tshirt.hashCode() : 0;
            result = 31 * result + (color != null ? color.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Record{" +
                    "tshirt=" + tshirt +
                    ", color=" + color +
                    '}';
        }
    }

    private static class TshirtMapper implements ResultMapper<Tshirt> {

        private final TableReference ref;

        private final ResultColumn id;
        private final ResultColumn size;
        private final ResultColumn colorId;

        private TshirtMapper(SelectQuery query) {
            ref = TSHIRT.refer();

            id = query.addToSelection(ref.get(TSHIRT_ID));
            size = query.addToSelection(ref.get(TSHIRT_SIZE));
            colorId = query.addToSelection(ref.get(TSHIRT_COLOR_ID));
        }

        @Override
        public Tshirt apply(ResultSet rs) throws SQLException {
            Integer id = JdbcUtils.readIntegerNull(rs, this.id.getIndex());
            return id == null ? null : new Tshirt(id,
                    JdbcUtils.readString(rs, size.getIndex()),
                    JdbcUtils.readIntegerNull(rs, colorId.getIndex()));
        }
    }

    private static class ColorMapper implements ResultMapper<Color> {

        private final TableReference ref;

        private final ResultColumn id;
        private final ResultColumn color;

        private ColorMapper(SelectQuery query) {
            ref = COLOR.refer();

            id = query.addToSelection(ref.get(COLOR_ID));
            color = query.addToSelection(ref.get(COLOR_COLOR));
        }

        @Override
        public Color apply(ResultSet rs) throws SQLException {
            Integer id = JdbcUtils.readIntegerNull(rs, this.id.getIndex());
            return id == null ? null : new Color(id,
                    JdbcUtils.readString(rs, color.getIndex()));
        }
    }

    private static class RecordMapper implements ResultMapper<Record> {

        private final TshirtMapper tshirtMapper;
        private final ColorMapper colorMapper;

        private RecordMapper(SelectQuery query) {
            tshirtMapper = new TshirtMapper(query);
            colorMapper = new ColorMapper(query);
        }

        @Override
        public Record apply(ResultSet rs) throws SQLException {
            return new Record(tshirtMapper.apply(rs), colorMapper.apply(rs));
        }
    }
}
