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
import io.zatarox.squiggle.criteria.InCriteria;
import io.zatarox.squiggle.criteria.MatchCriteria;

import static org.junit.Assert.assertThat;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.Test;

public class SubSelectTest {

    @Test
    public void testSubSelect() {
        Table people = new Table("people");
        Table taxcodes = new Table("taxcodes");

        SelectQuery select = new SelectQuery();
        select.addColumn(people, "firstname");

        SelectQuery subSelect = new SelectQuery();
        subSelect.addColumn(taxcodes, "id");
        subSelect.addCriteria(new MatchCriteria(taxcodes, "valid", MatchCriteria.EQUALS, true));

        select.addCriteria(new InCriteria(people, "taxcode", subSelect));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    people.firstname "
                + "FROM "
                + "    people "
                + "WHERE "
                + "    people.taxcode IN ( "
                + "        SELECT "
                + "            taxcodes.id "
                + "        FROM "
                + "            taxcodes "
                + "        WHERE "
                + "            taxcodes.valid = true "
                + "    )"));
    }
}
