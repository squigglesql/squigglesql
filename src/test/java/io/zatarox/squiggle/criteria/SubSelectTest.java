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

import io.zatarox.squiggle.SelectQuery;
import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.TableAccessor;
import io.zatarox.squiggle.TableColumn;
import io.zatarox.squiggle.literal.Literal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubSelectTest {

    @Test
    public void testSubSelect() {
        Table people = new Table("people");
        TableColumn firstName = people.getColumn("first_name");
        TableColumn taxCode = people.getColumn("tax_code");

        Table taxCodes = new Table("tax_codes");
        TableColumn taxCodeId = taxCodes.getColumn("id");
        TableColumn taxCodeValid = taxCodes.getColumn("valid");

        TableAccessor p = people.getAccessor("p");
        TableAccessor t = taxCodes.getAccessor("t");

        SelectQuery select = new SelectQuery();

        select.addToSelection(p.getColumn(firstName));

        SelectQuery subSelect = new SelectQuery();
        subSelect.addToSelection(t.getColumn(taxCodeId));
        subSelect.addCriteria(new MatchCriteria(t.getColumn(taxCodeValid), MatchCriteria.EQUALS, Literal.of(true)));

        select.addCriteria(new MatchCriteria(p.getColumn(taxCode), MatchCriteria.EQUALS, subSelect));

        assertEquals("SELECT\n"
                + "    p.first_name as a\n"
                + "FROM\n"
                + "    people p\n"
                + "WHERE\n"
                + "    p.tax_code = (\n"
                + "        SELECT\n"
                + "            t.id as a\n"
                + "        FROM\n"
                + "            tax_codes t\n"
                + "        WHERE\n"
                + "            t.valid = true\n"
                + "    )", select.toString());
    }
}
