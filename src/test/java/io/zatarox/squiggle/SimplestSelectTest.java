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
package io.zatarox.squiggle;

import io.zatarox.squiggle.query.ResultColumn;
import io.zatarox.squiggle.query.SelectQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimplestSelectTest {

    @Test
    public void simpleSelect() {
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.getColumn("first_name");
        TableColumn employeeLastName = employee.getColumn("last_name");
        TableColumn employeeAge = employee.getColumn("age");

        TableReference p = employee.createReference();

        SelectQuery select = new SelectQuery();

        select.addToSelection(p.getColumn(employeeFirstName));
        select.addToSelection(p.getColumn(employeeLastName));
        ResultColumn ageResult = select.addToSelection(p.getColumn(employeeAge));

        select.addOrder(ageResult, Order.DESCENDING);
        select.addOrder(new FunctionCall("concat",
                p.getColumn(employeeFirstName), p.getColumn(employeeLastName)), Order.ASCENDING);

        assertEquals("SELECT\n"
                + "    e.first_name as a,\n"
                + "    e.last_name as b,\n"
                + "    e.age as c\n"
                + "FROM\n"
                + "    employee e\n"
                + "ORDER BY\n"
                + "    c DESC,\n"
                + "    concat(e.first_name, e.last_name)", select.toString());
    }

    @Test
    public void simpleSelectDistinct() {
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.getColumn("first_name");
        TableColumn employeeLastName = employee.getColumn("last_name");

        TableReference p = employee.createReference();

        SelectQuery select = new SelectQuery(true);

        select.addToSelection(p.getColumn(employeeFirstName));
        select.addToSelection(p.getColumn(employeeLastName));

        assertEquals("SELECT DISTINCT\n"
                + "    e.first_name as a,\n"
                + "    e.last_name as b\n"
                + "FROM\n"
                + "    employee e", select.toString());
    }
}
