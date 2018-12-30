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
import io.zatarox.squiggle.literal.Literal;
import io.zatarox.squiggle.query.SelectQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NastyStringsTest {

    @Test
    public void testNastyStrings() {
        Table employee = new Table("employee");
        TableColumn employeeName = employee.getColumn("name");
        TableColumn employeeFee = employee.getColumn("foo");

        TableReference e = employee.createReference();

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.getColumn(employeeName));

        select.addCriteria(new MatchCriteria(e.getColumn(employeeFee), MatchCriteria.GREATER, Literal.of("I've got a quote")));

        assertEquals("SELECT\n"
                + "    e.name\n"
                + "FROM\n"
                + "    employee e\n"
                + "WHERE\n"
                + "    e.foo > 'I''ve got a quote'", select.toString());
    }
}
