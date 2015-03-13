/*
 * Copyright 2004-2015 Joe Walnes, Guillaume Chauvet.
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
package com.zatarox.squiggle;

import com.zatarox.squiggle.Order;
import com.zatarox.squiggle.SelectQuery;
import com.zatarox.squiggle.Table;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.hamcrest.text.IsEqualIgnoringWhiteSpace;

public class SimplestSelectTest {

    @Test
    public void simpleSelect() {
        Table people = new Table("people");

        SelectQuery select = new SelectQuery();

        select.addColumn(people, "firstname");
        select.addColumn(people, "lastname");

        select.addOrder(people, "age", Order.DESCENDING);

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT  people.firstname , people.lastname "
                + "FROM people "
                + "ORDER BY people.age DESC"));
    }
}
