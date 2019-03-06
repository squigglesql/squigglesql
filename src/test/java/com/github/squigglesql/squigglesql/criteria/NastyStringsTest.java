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
import com.github.squigglesql.squigglesql.literal.Literal;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NastyStringsTest {

    @Test
    public void testNastyStrings() {
        Table employee = new Table("employee");
        TableColumn employeeName = employee.get("name");
        TableColumn employeeFee = employee.get("foo");

        TableReference e = employee.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(e.get(employeeName));

        select.addCriteria(new MatchCriteria(e.get(employeeFee), MatchCriteria.GREATER, Literal.of("I've got a quote")));

        assertEquals("SELECT\n"
                + "    \"e\".\"name\"\n"
                + "FROM\n"
                + "    \"employee\" \"e\"\n"
                + "WHERE\n"
                + "    \"e\".\"foo\" > 'I''ve got a quote'", select.toString());
    }
}
