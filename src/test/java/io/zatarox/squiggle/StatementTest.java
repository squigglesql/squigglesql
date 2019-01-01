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

import io.zatarox.squiggle.criteria.MatchCriteria;
import io.zatarox.squiggle.mock.MockStatement;
import io.zatarox.squiggle.mock.MockStatementCompiler;
import io.zatarox.squiggle.parameter.IntegerParameter;
import io.zatarox.squiggle.query.SelectQuery;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class StatementTest {

    @Test
    public void testStatement() throws SQLException {
        Table employee = new Table("employee");
        TableColumn employeeName = employee.get("name");
        TableColumn employeeAge = employee.get("age");

        TableReference e = employee.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.get(employeeName));

        select.addCriteria(new MatchCriteria(
                e.get(employeeAge), MatchCriteria.LESS, new IntegerParameter(30)));

        MockStatement statement = select.toStatement(new MockStatementCompiler());

        assertEquals("SELECT\n"
                + "    e.name\n"
                + "FROM\n"
                + "    employee e\n"
                + "WHERE\n"
                + "    e.age < ?", statement.getQuery());

        assertEquals(1, statement.getParameters().size());
        assertEquals(30, statement.getParameters().get(0));
    }
}
