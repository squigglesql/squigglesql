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
package network.tide.squiggle.criteria;

import network.tide.squiggle.Table;
import network.tide.squiggle.TableColumn;
import network.tide.squiggle.TableReference;
import network.tide.squiggle.literal.Literal;
import network.tide.squiggle.query.SelectQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubSelectTest {

    @Test
    public void testSubSelect() {
        Table employee = new Table("employee");
        TableColumn employeeName = employee.get("name");
        TableColumn employeeTaxCode = employee.get("tax_code");

        Table taxCode = new Table("tax_code");
        TableColumn taxCodeId = taxCode.get("id");
        TableColumn taxCodeValid = taxCode.get("valid");

        TableReference e = employee.refer();
        TableReference t = taxCode.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.get(employeeName));

        SelectQuery subSelect = new SelectQuery();
        subSelect.addToSelection(t.get(taxCodeId));
        subSelect.addCriteria(new MatchCriteria(t.get(taxCodeValid), MatchCriteria.EQUALS, Literal.of(true)));

        select.addCriteria(new MatchCriteria(e.get(employeeTaxCode), MatchCriteria.EQUALS, subSelect));

        assertEquals("SELECT\n"
                + "    \"e\".\"name\"\n"
                + "FROM\n"
                + "    \"employee\" \"e\"\n"
                + "WHERE\n"
                + "    \"e\".\"tax_code\" = (\n"
                + "        SELECT\n"
                + "            \"t\".\"id\"\n"
                + "        FROM\n"
                + "            \"tax_code\" \"t\"\n"
                + "        WHERE\n"
                + "            \"t\".\"valid\" = true\n"
                + "    )", select.toString());
    }
}
