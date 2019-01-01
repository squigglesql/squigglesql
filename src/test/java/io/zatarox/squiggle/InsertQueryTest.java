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

import io.zatarox.squiggle.literal.Literal;
import io.zatarox.squiggle.mock.MockStatement;
import io.zatarox.squiggle.mock.MockStatementCompiler;
import io.zatarox.squiggle.parameter.IntegerParameter;
import io.zatarox.squiggle.parameter.StringParameter;
import io.zatarox.squiggle.query.InsertQuery;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class InsertQueryTest {

    @Test
    public void testInsertQuery() throws SQLException {
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.get("first_name");
        TableColumn employeeLastName = employee.get("last_name");
        TableColumn employeeAge = employee.get("age");

        InsertQuery query = new InsertQuery(employee);

        query.addValue(employeeFirstName, new StringParameter("John"));
        query.addValue(employeeLastName, Literal.of("Smith"));
        query.addValue(employeeAge, new IntegerParameter(30));

        MockStatement statement = query.toStatement(new MockStatementCompiler());

        assertEquals("INSERT INTO employee(first_name, last_name, age) "
                + "VALUES (?, 'Smith', ?)", statement.getQuery());
        assertEquals(2, statement.getParameters().size());
        assertEquals("John", statement.getParameters().get(0));
        assertEquals(30, statement.getParameters().get(1));
    }
}
