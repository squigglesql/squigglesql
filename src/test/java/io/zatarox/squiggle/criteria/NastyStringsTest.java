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
package io.zatarox.squiggle.criteria;

import io.zatarox.squiggle.SelectQuery;
import io.zatarox.squiggle.Table;

import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class NastyStringsTest {

    @Test
    public void testNastyStrings() {
        Table people = new Table("people");

        SelectQuery select = new SelectQuery();

        select.addColumn(people, "firstname");

        select.addCriteria(
                new MatchCriteria(people, "foo", MatchCriteria.GREATER, "I've got a quote"));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT\n"
                + "    people.firstname\n"
                + "FROM\n"
                + "    people\n"
                + "WHERE\n"
                + "    people.foo > 'I\\'ve got a quote'"));
    }
}
