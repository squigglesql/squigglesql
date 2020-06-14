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
package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.criteria.InCriteria;
import com.github.squigglesql.squigglesql.criteria.MatchCriteria;
import com.github.squigglesql.squigglesql.databases.TestDatabaseColumn;
import com.github.squigglesql.squigglesql.join.QualifiedJoin;
import com.github.squigglesql.squigglesql.join.QualifiedJoinKind;
import com.github.squigglesql.squigglesql.literal.Literal;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.statement.JdbcStatementCompiler;
import com.github.squigglesql.squigglesql.util.JdbcUtils;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class TutorialTest {

    @Test
    public void testTutorial() {
        // define tables
        Table order = new Table("ordr");
        TableColumn orderId = order.get("id");
        TableColumn orderTotalPrice = order.get("total_price");
        TableColumn orderStatus = order.get("status");
        TableColumn orderItems = order.get("items");
        TableColumn orderDelivery = order.get("delivery");
        TableColumn orderWarehouseId = order.get("warehouse_id");

        Table warehouse = new Table("warehouse");
        TableColumn warehouseId = warehouse.get("id");
        TableColumn warehouseSize = warehouse.get("size");
        TableColumn warehouseLocation = warehouse.get("location");

        Table offer = new Table("offer");
        TableColumn offerLocation = offer.get("location");
        TableColumn offerValid = offer.get("valid");

        // basic query
        TableReference o = order.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(o.get(orderId));
        select.addToSelection(o.get(orderTotalPrice));

        // matches
        select.addCriteria(new MatchCriteria(
                o.get(orderStatus), MatchCriteria.EQUALS, new TypeCast(Literal.of("processed"), "status")));
        select.addCriteria(new MatchCriteria(
                o.get(orderItems), MatchCriteria.LESS, Literal.of(5)));
        select.addCriteria(new InCriteria(o.get(orderDelivery),
                Literal.of("post"), Literal.of("fedex"), Literal.of("goat")));

        // join
        TableReference w = warehouse.refer();

        select.addFrom(new QualifiedJoin(o, QualifiedJoinKind.INNER, w,
                new MatchCriteria(o.get(orderWarehouseId), MatchCriteria.EQUALS, w.get(warehouseId))));

        // use joined table
        select.addToSelection(w.get(warehouseLocation));
        select.addCriteria(new MatchCriteria(
                w.get(warehouseSize), MatchCriteria.EQUALS, Literal.of("big")));

        // build subselect query
        TableReference f = offer.refer();

        SelectQuery subSelect = new SelectQuery();

        subSelect.addToSelection(f.get(offerLocation));
        subSelect.addCriteria(new MatchCriteria(
                f.get(offerValid), MatchCriteria.EQUALS, Literal.of(true)));

        // add subselect to original query
        select.addCriteria(new InCriteria(w.get(warehouseLocation), subSelect));

        assertEquals("SELECT\n"
                + "    o.id,\n"
                + "    o.total_price,\n"
                + "    w.location\n"
                + "FROM\n"
                + "    ordr o\n"
                + "    INNER JOIN warehouse w ON o.warehouse_id = w.id\n"
                + "WHERE\n"
                + "    o.status = 'processed'::status AND\n"
                + "    o.items < 5 AND\n"
                + "    o.delivery IN ('post', 'fedex', 'goat') AND\n"
                + "    w.size = 'big' AND\n"
                + "    w.location IN ((\n"
                + "        SELECT\n"
                + "            o.location\n"
                + "        FROM\n"
                + "            offer o\n"
                + "        WHERE\n"
                + "            o.valid = true\n"
                + "    ))", select.toString());
    }

    @Test
    public void testDemo() {
        // define table
        Table employee = new Table("employee");
        TableColumn employeeFirstName = employee.get("firstname");
        TableColumn employeeLastName = employee.get("lastname");
        TableColumn employeeAge = employee.get("age");

        // build query
        TableReference e = employee.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.get(employeeFirstName));
        select.addToSelection(e.get(employeeLastName));

        select.addCriteria(new MatchCriteria(e.get(employeeAge), MatchCriteria.GREATEREQUAL, Literal.of(18)));

        select.addOrder(e.get(employeeAge), Order.DESCENDING);

        assertEquals("SELECT\n"
                + "    e.firstname,\n"
                + "    e.lastname\n"
                + "FROM\n"
                + "    employee e\n"
                + "WHERE\n"
                + "    e.age >= 18\n"
                + "ORDER BY\n"
                + "    e.age DESC", select.toString());
    }

    @Test
    public void testJdbcUtilsTutorial() throws Exception {
        TestUtils.withDatabase((connection, database) -> {
            TestUtils.withTable(connection, database, "employee", new TestDatabaseColumn[] {
                    new TestDatabaseColumn("name", "TEXT", true, null),
                    new TestDatabaseColumn("age", "INT", true, null)
            }, () -> {
                Table employee = new Table("employee");
                TableColumn employeeId = employee.get("id");
                TableColumn employeeName = employee.get("name");
                TableColumn employeeAge = employee.get("age");

                InsertQuery insert = new InsertQuery(employee);
                insert.addValue(employeeName, Parameter.of("Homer"));
                insert.addValue(employeeAge, Parameter.of(40));
                assertEquals((Integer) 1, JdbcUtils.insert(insert, connection, rs -> rs.getInt(1)));

                TableReference e = employee.refer();

                SelectQuery select = new SelectQuery();

                ResultColumn employeeIdResult = select.addToSelection(e.get(employeeId));
                ResultColumn employeeNameResult = select.addToSelection(e.get(employeeName));
                ResultColumn employeeAgeResult = select.addToSelection(e.get(employeeAge));

                try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
                    try (ResultSet rs = statement.executeQuery()) {
                        rs.next();
                        int id = JdbcUtils.readIntegerNotNull(rs, employeeIdResult.getIndex());
                        String name = JdbcUtils.readString(rs, employeeNameResult.getIndex());
                        int age = JdbcUtils.readIntegerNotNull(rs, employeeAgeResult.getIndex());
                        assertEquals(1, id);
                        assertEquals("Homer", name);
                        assertEquals(40, age);
                    }
                }

                Employee result = JdbcUtils.selectOne(select, connection, rs -> new Employee(
                        JdbcUtils.readIntegerNotNull(rs, employeeIdResult.getIndex()),
                        JdbcUtils.readString(rs, employeeNameResult.getIndex()),
                        JdbcUtils.readIntegerNotNull(rs, employeeAgeResult.getIndex())));
                assertEquals(1, result.getId());
                assertEquals("Homer", result.getName());
                assertEquals(40, result.getAge());

                return null;
            });
        });
    }

    private static class Employee {

        private final int id;
        private final String name;
        private final int age;

        public Employee(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
