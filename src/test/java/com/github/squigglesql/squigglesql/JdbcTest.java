package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.criteria.MatchCriteria;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.query.UpdateQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;
import com.github.squigglesql.squigglesql.util.TooManyRecordsException;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import static com.github.squigglesql.squigglesql.TestUtils.*;
import static com.github.squigglesql.squigglesql.criteria.MatchCriteria.EQUALS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdbcTest {

    private static final Table TABLE = new Table("employee");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn NAME = TABLE.get("name");
    private static final TableColumn AGE = TABLE.get("age");
    private static final String SEQUENCE = "employee_id_seq";

    private static final Employee AARON = new Employee(1, "Aaron", 20);
    private static final Employee BOB = new Employee(2, "Bob", 30);
    private static final Employee BOB_GROWN = BOB.withAge(31);
    private static final int NONE_ID = 3;

    @Test
    public void testSelectOne() throws SQLException {
        withContents(connection -> {
            SelectQuery query = new SelectQuery();
            ResultMapper<Employee> mapper = addToQuery(query, AARON.getId());
            Employee employee = JdbcUtils.selectOne(query, connection, mapper);
            assertEquals(AARON, employee);
        });
    }

    @Test
    public void testSelectOneWithEmpty() throws SQLException {
        withContents(connection -> {
            SelectQuery query = new SelectQuery();
            ResultMapper<Employee> mapper = addToQuery(query, NONE_ID);
            Employee employee = JdbcUtils.selectOne(query, connection, mapper);
            assertNull(employee);
        });
    }

    @Test(expected = TooManyRecordsException.class)
    public void testSelectOneWithMany() throws SQLException {
        withContents(connection -> {
            SelectQuery query = new SelectQuery();
            ResultMapper<Employee> mapper = addToQuery(query);
            Employee employee = JdbcUtils.selectOne(query, connection, mapper);
            assertNull(employee);
        });
    }

    @Test
    public void testSelectAll() throws SQLException {
        withContents(connection -> {
            SelectQuery query = new SelectQuery();
            ResultMapper<Employee> mapper = addToQuery(query);
            List<Employee> actual = JdbcUtils.selectAll(query, connection, mapper);
            List<Employee> expected = new ArrayList<>();
            expected.add(AARON);
            expected.add(BOB);
            assertEquals(expected, actual);
        });
    }

    @Test
    public void testUpdate() throws SQLException {
        withContents(connection -> {
            TableReference ref = TABLE.refer();
            UpdateQuery updateQuery = new UpdateQuery(ref);
            updateQuery.addValue(AGE, Parameter.of(BOB_GROWN.getAge()));
            updateQuery.addCriteria(new MatchCriteria(ref.get(ID), EQUALS, Parameter.of(BOB.getId())));
            JdbcUtils.update(updateQuery, connection);

            SelectQuery selectQuery = new SelectQuery();
            ResultMapper<Employee> mapper = addToQuery(selectQuery, BOB.getId());
            Employee employee = JdbcUtils.selectOne(selectQuery, connection, mapper);
            assertEquals(BOB_GROWN, employee);
        });
    }

    private static void withContents(Consumer consumer) throws SQLException {
        String createQuery = "CREATE TABLE " + TABLE.getName() + " (\n" +
                ID.getName() + " INTEGER DEFAULT nextval('" + SEQUENCE + "'::regclass) NOT NULL,\n" +
                NAME.getName() + " TEXT NOT NULL,\n" +
                AGE.getName() + " INTEGER NOT NULL\n" +
                ")";
        withDatabase(
                connection -> withSequence(connection, SEQUENCE,
                        () -> withTable(connection, TABLE.getName(), createQuery,
                                () -> {
                                    insert(connection, AARON);
                                    insert(connection, BOB);
                                    consumer.accept(connection);
                                    return null;
                                })));
    }

    private static void insert(Connection connection, Employee employee) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(NAME, Parameter.of(employee.getName()));
        query.addValue(AGE, Parameter.of(employee.getAge()));
        JdbcUtils.insert(query, connection);
    }

    private static ResultMapper<Employee> addToQuery(SelectQuery query) {
        return addToQuery(query, (ref, key) -> query.addOrder(ref.get(ID), Order.ASCENDING));
    }

    private static ResultMapper<Employee> addToQuery(SelectQuery query, int id) {
        return addToQuery(query, (ref, key) -> {
            query.addCriteria(new MatchCriteria(key, EQUALS, Parameter.of(id)));
        });
    }

    private static ResultMapper<Employee> addToQuery(SelectQuery query, BiConsumer<TableReference, Matchable> joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn name = query.addToSelection(ref.get(NAME));
        ResultColumn age = query.addToSelection(ref.get(AGE));

        joiner.accept(ref, ref.get(ID));

        return rs -> new Employee(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readString(rs, name.getIndex()),
                JdbcUtils.readIntegerNotNull(rs, age.getIndex()));
    }

    private static class Employee {

        private final int id;
        private final String name;
        private final int age;

        public Employee(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Employee withAge(int age) {
            return new Employee(id, name, age);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return id == employee.id &&
                    age == employee.age &&
                    name.equals(employee.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, age);
        }
    }
}
