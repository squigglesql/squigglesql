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
        TableColumn firstName = employee.getColumn("first_name");
        TableColumn lastName = employee.getColumn("last_name");
        TableColumn height = employee.getColumn("height");
        TableColumn department = employee.getColumn("department");
        TableColumn age = employee.getColumn("age");

        TableReference e = employee.createReference("e");

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.getColumn(firstName));
        select.addToSelection(e.getColumn(lastName));

        select.addCriteria(new MatchCriteria(e.getColumn(height), MatchCriteria.GREATER, Literal.of(1.8)));
        select.addCriteria(new InCriteria(e.getColumn(department), Literal.of("I.T."), Literal.of("Cooking")));
        select.addCriteria(new BetweenCriteria(e.getColumn(age), Literal.of(18), Literal.of(30)));

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
        TableColumn name = employee.getColumn("name");
        TableColumn age = employee.getColumn("age");

        TableReference e = employee.createReference("e");

        SelectQuery select = new SelectQuery();

        select.addCriteria(new IsNullCriteria(e.getColumn(name)));
        select.addCriteria(new IsNotNullCriteria(e.getColumn(age)));

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
        TableColumn name = river.getColumn("name");
        TableColumn level = river.getColumn("level");
        TableColumn lowerLimit = river.getColumn("lower_limit");
        TableColumn upperLimit = river.getColumn("upper_limit");

        TableReference r = river.createReference("r");

        SelectQuery select = new SelectQuery();

        select.addToSelection(r.getColumn(name));
        select.addToSelection(r.getColumn(level));

        select.addCriteria(new BetweenCriteria(r.getColumn(level), r.getColumn(lowerLimit), r.getColumn(upperLimit)));

        assertEquals("SELECT\n"
                + "    r.name as a,\n"
                + "    r.level as b\n"
                + "FROM\n"
                + "    river r\n"
                + "WHERE\n"
                + "    r.level BETWEEN r.lower_limit AND r.upper_limit", select.toString());
    }
}
