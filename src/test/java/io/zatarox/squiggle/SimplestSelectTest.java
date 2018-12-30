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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimplestSelectTest {

    @Test
    public void simpleSelect() {
        Table people = new Table("people");
        TableColumn firstName = people.getColumn("first_name");
        TableColumn lastName = people.getColumn("last_name");
        TableColumn age = people.getColumn("age");

        TableAccessor p = people.getAccessor("p");

        SelectQuery select = new SelectQuery();

        select.addToSelection(p.getColumn(firstName));
        select.addToSelection(p.getColumn(lastName));
        ResultColumn ageResult = select.addToSelection(p.getColumn(age));

        select.addOrder(ageResult, Order.DESCENDING);

        assertEquals("SELECT\n"
                + "    p.first_name as a,\n"
                + "    p.last_name as b,\n"
                + "    p.age as c\n"
                + "FROM\n"
                + "    people p\n"
                + "ORDER BY\n"
                + "    c DESC", select.toString());
    }

    @Test
    public void simpleSelectDistinct() {
        Table people = new Table("people");
        TableColumn firstName = people.getColumn("first_name");
        TableColumn lastName = people.getColumn("last_name");
        TableColumn age = people.getColumn("age");

        TableAccessor p = people.getAccessor("p");

        SelectQuery select = new SelectQuery(true);

        select.addToSelection(p.getColumn(firstName));
        select.addToSelection(p.getColumn(lastName));
        ResultColumn ageResult = select.addToSelection(p.getColumn(age));

        select.addOrder(ageResult, Order.DESCENDING);

        assertEquals("SELECT DISTINCT\n"
                + "    p.first_name as a,\n"
                + "    p.last_name as b,\n"
                + "    p.age as c\n"
                + "FROM\n"
                + "    people p\n"
                + "ORDER BY\n"
                + "    c DESC", select.toString());
    }
}
