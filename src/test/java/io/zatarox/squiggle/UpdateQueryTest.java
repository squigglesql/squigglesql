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
import io.zatarox.squiggle.parameter.Parameter;
import io.zatarox.squiggle.query.UpdateQuery;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class UpdateQueryTest {

    @Test
    public void testUpdateQuery() throws SQLException {
        Table employee = new Table("employee");
        TableColumn employeeId = employee.get("id");
        TableColumn employeeStatus = employee.get("status");
        TableColumn employeeStatusChangedAt = employee.get("status_changed_at");

        Table session = new Table("session");
        TableColumn sessionId = session.get("id");
        TableColumn sessionEmployeeId = session.get("employee_id");

        TableReference e = employee.refer();
        TableReference s = session.refer();

        UpdateQuery query = new UpdateQuery(e);

        query.addValue(employeeStatus, Parameter.of("BLOCKED"));
        query.addValue(employeeStatusChangedAt, new FunctionCall("now"));

        query.addCriteria(new MatchCriteria(
                e.get(employeeId), MatchCriteria.EQUALS, s.get(sessionEmployeeId)));
        query.addCriteria(new MatchCriteria(
                s.get(sessionId), MatchCriteria.EQUALS, Parameter.of("<session_id_value>")));

        MockStatement statement = query.toStatement(new MockStatementCompiler());

        assertEquals("UPDATE employee e SET\n"
                + "    status = ?,\n"
                + "    status_changed_at = now()\n"
                + "FROM\n"
                + "    session s\n"
                + "WHERE\n"
                + "    e.id = s.employee_id AND\n"
                + "    s.id = ?", statement.getQuery());
        assertEquals(2, statement.getParameters().size());
        assertEquals("BLOCKED", statement.getParameters().get(0));
        assertEquals("<session_id_value>", statement.getParameters().get(1));
    }
}
