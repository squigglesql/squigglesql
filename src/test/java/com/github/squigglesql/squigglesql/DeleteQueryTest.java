/*
 * Copyright 2021 Cody Ebberson.
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

import com.github.squigglesql.squigglesql.mock.MockStatement;
import com.github.squigglesql.squigglesql.mock.MockStatementCompiler;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.DeleteQuery;
import org.junit.Test;

import java.sql.SQLException;

import static com.github.squigglesql.squigglesql.criteria.Criteria.equal;
import static org.junit.Assert.assertEquals;

public class DeleteQueryTest {

    @Test
    public void testDeleteQuery() throws SQLException {
        Table employee = new Table("employee");
        TableColumn employeeId = employee.get("id");

        TableReference e = employee.refer();

        DeleteQuery query = new DeleteQuery(e);
        query.addCriteria(equal(e.get(employeeId), Parameter.of("<employee_id_value>")));

        MockStatement statement = query.toStatement(new MockStatementCompiler());

        assertEquals("DELETE FROM\n"
                + "    employee e\n"
                + "WHERE\n"
                + "    e.id = ?", statement.getQuery());
        assertEquals(1, statement.getParameters().size());
        assertEquals("<employee_id_value>", statement.getParameters().get(0));
    }

    @Test()
    public void testEmptyDelete() throws SQLException {
        Table employee = new Table("employee");
        TableReference e = employee.refer();
        DeleteQuery query = new DeleteQuery(e);

        MockStatement statement = query.toStatement(new MockStatementCompiler());

        assertEquals("DELETE FROM\n"
                + "    employee e", statement.getQuery());
        assertEquals(0, statement.getParameters().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTableReference() {
        new DeleteQuery(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCriteria() {
        Table employee = new Table("employee");
        TableReference e = employee.refer();
        new DeleteQuery(e).addCriteria(null);
    }
}
