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

public class WhereCriteriaTest {

    @Test
    public void whereCriteria() {
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.getColumn("first_name");
        TableColumn employeeLastName = employee.getColumn("last_name");
        TableColumn employeeHeight = employee.getColumn("height");
        TableColumn employeeDepartment = employee.getColumn("department");
        TableColumn employeeAge = employee.getColumn("age");

        TableReference e = employee.createReference();

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.getColumn(employeeFirstName));
        select.addToSelection(e.getColumn(employeeLastName));

        select.addCriteria(new MatchCriteria(e.getColumn(employeeHeight), MatchCriteria.GREATER, Literal.of(1.8)));
        select.addCriteria(new InCriteria(e.getColumn(employeeDepartment), Literal.of("I.T."), Literal.of("Cooking")));
        select.addCriteria(new BetweenCriteria(e.getColumn(employeeAge), Literal.of(18), Literal.of(30)));

        assertEquals("SELECT\n"
                + "    e.first_name as a,\n"
                + "    e.last_name as b\n"
                + "FROM\n"
                + "    employee e\n"
                + "WHERE\n"
                + "    e.height > 1.8 AND\n"
                + "    e.department IN ('I.T.', 'Cooking') AND\n"
                + "    e.age BETWEEN 18 AND 30", select.toString());

    }

    @Test
    public void nullCriteria() {
        Table employee = new Table("employee");
        TableColumn employeeName = employee.getColumn("name");
        TableColumn employeeAge = employee.getColumn("age");

        TableReference e = employee.createReference();

        SelectQuery select = new SelectQuery();

        select.addCriteria(new IsNullCriteria(e.getColumn(employeeName)));
        select.addCriteria(new IsNotNullCriteria(e.getColumn(employeeAge)));

        assertEquals("SELECT 1\n"
                + "FROM\n"
                + "    employee e\n"
                + "WHERE\n"
                + "    e.name IS NULL AND\n"
                + "    e.age IS NOT NULL", select.toString());
    }

    @Test
    public void betweenCriteriaWithColumns() {
        Table river = new Table("river");
        TableColumn riverName = river.getColumn("name");
        TableColumn riverLevel = river.getColumn("level");
        TableColumn riverLowerLimit = river.getColumn("lower_limit");
        TableColumn riverUpperLimit = river.getColumn("upper_limit");

        TableReference r = river.createReference();

        SelectQuery select = new SelectQuery();

        select.addToSelection(r.getColumn(riverName));
        select.addToSelection(r.getColumn(riverLevel));

        select.addCriteria(new BetweenCriteria(r.getColumn(riverLevel), r.getColumn(riverLowerLimit), r.getColumn(riverUpperLimit)));

        assertEquals("SELECT\n"
                + "    r.name as a,\n"
                + "    r.level as b\n"
                + "FROM\n"
                + "    river r\n"
                + "WHERE\n"
                + "    r.level BETWEEN r.lower_limit AND r.upper_limit", select.toString());
    }
}
