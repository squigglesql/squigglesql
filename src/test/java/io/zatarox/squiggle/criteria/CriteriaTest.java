/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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
package io.zatarox.squiggle.criteria;

import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.TableColumn;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.literal.Literal;
import io.zatarox.squiggle.query.SelectQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CriteriaTest {

    @Test
    public void testCriteria() {
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.get("first_name");
        TableColumn employeeLastName = employee.get("last_name");
        TableColumn employeeHeight = employee.get("height");
        TableColumn employeeDepartment = employee.get("department");
        TableColumn employeeAge = employee.get("age");

        TableReference e = employee.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.get(employeeFirstName));
        select.addToSelection(e.get(employeeLastName));

        select.addCriteria(new MatchCriteria(e.get(employeeHeight), MatchCriteria.GREATER, Literal.of(1.8)));
        select.addCriteria(new InCriteria(e.get(employeeDepartment), Literal.of("I.T."), Literal.of("Cooking")));
        select.addCriteria(new BetweenCriteria(e.get(employeeAge), Literal.of(18), Literal.of(30)));

        assertEquals("SELECT\n"
                + "    e.first_name,\n"
                + "    e.last_name\n"
                + "FROM\n"
                + "    employee e\n"
                + "WHERE\n"
                + "    e.height > 1.8 AND\n"
                + "    e.department IN ('I.T.', 'Cooking') AND\n"
                + "    e.age BETWEEN 18 AND 30", select.toString());

    }

    @Test
    public void testNullCriteria() {
        Table employee = new Table("employee");
        TableColumn employeeName = employee.get("name");
        TableColumn employeeAge = employee.get("age");

        TableReference e = employee.refer();

        SelectQuery select = new SelectQuery();

        select.addCriteria(new IsNullCriteria(e.get(employeeName)));
        select.addCriteria(new IsNotNullCriteria(e.get(employeeAge)));

        assertEquals("SELECT 1\n"
                + "FROM\n"
                + "    employee e\n"
                + "WHERE\n"
                + "    e.name IS NULL AND\n"
                + "    e.age IS NOT NULL", select.toString());
    }

    @Test
    public void testNotAndBetweenCriteriaWithColumns() {
        Table river = new Table("river");
        TableColumn riverName = river.get("name");
        TableColumn riverLevel = river.get("level");
        TableColumn riverLowerLimit = river.get("lower_limit");
        TableColumn riverUpperLimit = river.get("upper_limit");

        TableReference r = river.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(r.get(riverName));
        select.addToSelection(r.get(riverLevel));

        select.addCriteria(new NotCriteria(
                new BetweenCriteria(r.get(riverLevel), r.get(riverLowerLimit), r.get(riverUpperLimit))));

        assertEquals("SELECT\n"
                + "    r.name,\n"
                + "    r.level\n"
                + "FROM\n"
                + "    river r\n"
                + "WHERE\n"
                + "    NOT (r.level BETWEEN r.lower_limit AND r.upper_limit)", select.toString());
    }
}
