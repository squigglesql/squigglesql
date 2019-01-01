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

public class SelectTest {

    @Test
    public void testSimpleSelect() {
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.get("first_name");
        TableColumn employeeLastName = employee.get("last_name");
        TableColumn employeeAge = employee.get("age");

        TableReference p = employee.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(p.get(employeeFirstName));
        select.addToSelection(p.get(employeeLastName));
        ResultColumn ageResult = select.addToSelection(p.get(employeeAge));

        select.addOrder(ageResult, Order.DESCENDING);
        select.addOrder(new FunctionCall("concat",
                p.get(employeeFirstName), p.get(employeeLastName)), Order.ASCENDING);

        assertEquals("SELECT\n"
                + "    e.first_name,\n"
                + "    e.last_name,\n"
                + "    e.age as a\n"
                + "FROM\n"
                + "    employee e\n"
                + "ORDER BY\n"
                + "    a DESC,\n"
                + "    concat(e.first_name, e.last_name)", select.toString());
    }

    @Test
    public void testSelectDistinct() {
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.get("first_name");
        TableColumn employeeLastName = employee.get("last_name");

        TableReference p = employee.refer();

        SelectQuery select = new SelectQuery(true);

        select.addToSelection(p.get(employeeFirstName));
        select.addToSelection(p.get(employeeLastName));

        assertEquals("SELECT DISTINCT\n"
                + "    e.first_name,\n"
                + "    e.last_name\n"
                + "FROM\n"
                + "    employee e", select.toString());
    }
}
