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
import io.zatarox.squiggle.query.SelectQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JoinTest {

    @Test
    public void joinOnForeignKeyRelationship() {
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.getColumn("first_name");
        TableColumn employeeDepartmentId = employee.getColumn("department_id");

        Table department = new Table("department");
        TableColumn departmentId = department.getColumn("id");
        TableColumn departmentDirector = department.getColumn("director");

        TableReference e = employee.createReference();
        TableReference d = department.createReference();

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.getColumn(employeeFirstName));
        select.addToSelection(d.getColumn(departmentDirector));

        select.addCriteria(new MatchCriteria(
                e.getColumn(employeeDepartmentId), MatchCriteria.EQUALS, d.getColumn(departmentId)));

        assertEquals("SELECT\n"
                + "    e.first_name as a,\n"
                + "    d.director as b\n"
                + "FROM\n"
                + "    employee e,\n"
                + "    department d\n"
                + "WHERE\n"
                + "    e.department_id = d.id", select.toString());
    }

    @Test
    public void joinOnComparison() {
        Table invoice = new Table("invoice");
        TableColumn invoiceNumber = invoice.getColumn("number");
        TableColumn invoiceDate = invoice.getColumn("date");

        Table taxPayment = new Table("tax_payment");
        TableColumn taxPaymentDate = taxPayment.getColumn("date");

        TableReference i = invoice.createReference();
        TableReference t = taxPayment.createReference();

        SelectQuery select = new SelectQuery();

        select.addToSelection(i.getColumn(invoiceNumber));

        select.addCriteria(new MatchCriteria(
                i.getColumn(invoiceDate), MatchCriteria.GREATER, t.getColumn(taxPaymentDate)));

        assertEquals("SELECT\n"
                + "    i.number as a\n"
                + "FROM\n"
                + "    invoice i,\n"
                + "    tax_payment t\n"
                + "WHERE\n"
                + "    i.date > t.date", select.toString());
    }
}
