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

import com.github.squigglesql.squigglesql.literal.Literal;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import org.junit.Test;

import static com.github.squigglesql.squigglesql.criteria.Criteria.between;
import static org.junit.Assert.assertEquals;

public class FunctionsTest {

    @Test
    public void testFunctions() {
        Table table = new Table("table");
        TableColumn column = table.get("column");

        TableReference t = table.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(new FunctionCall("sheep"));
        select.addToSelection(new FunctionCall("cheese", Literal.of(10)));
        select.addToSelection(new FunctionCall("tomato", Literal.of("red"), t.get(column)));

        assertEquals("SELECT\n"
                + "    sheep(),\n"
                + "    cheese(10),\n"
                + "    tomato('red', t.column)\n"
                + "FROM\n"
                + "    table t", select.toString());
    }

    @Test
    public void testFunctionsInCriteria() {
        Table card = new Table("credit_card");
        TableColumn cardNumber = card.get("number");
        TableColumn cardIssue = card.get("issue");
        TableColumn cardIssueDate = card.get("issue_date");
        TableColumn cardExpiryDate = card.get("expiry_date");

        TableReference c = card.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(c.get(cardNumber));
        select.addToSelection(c.get(cardIssue));

        select.addCriteria(between(new FunctionCall("getDate"), c.get(cardIssueDate), c.get(cardExpiryDate)));

        assertEquals("SELECT\n"
                + "    c.number,\n"
                + "    c.issue\n"
                + "FROM\n"
                + "    credit_card c\n"
                + "WHERE\n"
                + "    getDate() BETWEEN c.issue_date AND c.expiry_date", select.toString());
    }

    @Test
    public void testFunctionThatDoesNotReferToTables() {
        SelectQuery select = new SelectQuery();
        select.addToSelection(new FunctionCall("getdate"));

        assertEquals("SELECT\n"
                + "    getdate()", select.toString());
    }
}
