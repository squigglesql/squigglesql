package io.zatarox.squiggle;

import io.zatarox.squiggle.criteria.MatchCriteria;
import io.zatarox.squiggle.parameter.IntegerParameter;
import io.zatarox.squiggle.query.SelectQuery;
import io.zatarox.squiggle.statement.StatementBuilder;
import io.zatarox.squiggle.statement.StatementCompiler;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StatementTest {

    @Test
    public void testStatement() throws SQLException {
        Table employee = new Table("employee");
        TableColumn name = employee.getColumn("name");
        TableColumn age = employee.getColumn("age");

        TableReference e = employee.createReference("e");

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.getColumn(name));

        select.addCriteria(new MatchCriteria(e.getColumn(age), MatchCriteria.LESS, new IntegerParameter(30)));

        MockStatement statement = select.toStatement(new MockStatementCompiler());

        assertEquals("SELECT\n"
                + "    e.name as a\n"
                + "FROM\n"
                + "    employee e\n"
                + "WHERE\n"
                + "    e.age < ?", statement.getQuery());

        assertEquals(1, statement.getParameters().size());
        assertEquals(30, statement.getParameters().get(0));
    }

    private static class MockStatementCompiler implements StatementCompiler<MockStatement> {

        @Override
        public StatementBuilder<MockStatement> createStatementBuilder(String query) {
            return new MockStatement(query);
        }
    }

    // Builder for itself...
    private static class MockStatement implements StatementBuilder<MockStatement> {

        private final String query;
        private final List<Object> parameters = new ArrayList<Object>();

        public MockStatement(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }

        public List<Object> getParameters() {
            return parameters;
        }

        @Override
        public MockStatement buildStatement() {
            return this;
        }

        @Override
        public void addBoolean(Boolean value) {
            parameters.add(value);
        }

        @Override
        public void addInteger(Integer value) {
            parameters.add(value);
        }

        @Override
        public void addString(String value) {
            parameters.add(value);
        }
    }
}
