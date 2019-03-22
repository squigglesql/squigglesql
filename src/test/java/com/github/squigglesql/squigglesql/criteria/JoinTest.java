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
package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JoinTest {

    @Test
    public void testJoinBySelection() {
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.get("first_name");
        TableColumn employeeDepartmentId = employee.get("department_id");

        Table department = new Table("department");
        TableColumn departmentId = department.get("id");
        TableColumn departmentDirector = department.get("director");

        TableReference e = employee.refer();
        TableReference d = department.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.get(employeeFirstName));
        select.addToSelection(d.get(departmentDirector));

        select.addCriteria(new MatchCriteria(
                e.get(employeeDepartmentId), MatchCriteria.EQUALS, d.get(departmentId)));

        assertEquals("SELECT\n"
                + "    e.first_name,\n"
                + "    d.director\n"
                + "FROM\n"
                + "    employee e,\n"
                + "    department d\n"
                + "WHERE\n"
                + "    e.department_id = d.id", select.toString());
    }

    @Test
    public void testJoinByCriteria() {
        Table invoice = new Table("invoice");
        TableColumn invoiceNumber = invoice.get("number");
        TableColumn invoiceDate = invoice.get("date");

        Table taxPayment = new Table("tax_payment");
        TableColumn taxPaymentDate = taxPayment.get("date");

        TableReference i = invoice.refer();
        TableReference t = taxPayment.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(i.get(invoiceNumber));

        select.addCriteria(new MatchCriteria(
                i.get(invoiceDate), MatchCriteria.GREATER, t.get(taxPaymentDate)));

        assertEquals("SELECT\n"
                + "    i.number\n"
                + "FROM\n"
                + "    invoice i,\n"
                + "    tax_payment t\n"
                + "WHERE\n"
                + "    i.date > t.date", select.toString());
    }
}
