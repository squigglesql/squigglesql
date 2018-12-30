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

import io.zatarox.squiggle.criteria.BetweenCriteria;
import io.zatarox.squiggle.literal.Literal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FunctionsTest {

    @Test
    public void functions() {
        Table table = new Table("table");
        TableColumn column = table.getColumn("column");

        TableAccessor t = table.getAccessor("t");

        SelectQuery select = new SelectQuery();

        select.addToSelection(new FunctionCall("sheep"));
        select.addToSelection(new FunctionCall("cheese", Literal.of(10)));
        select.addToSelection(new FunctionCall("tomato", Literal.of("red"), t.getColumn(column)));

        assertEquals("SELECT\n"
                + "    sheep() as a,\n"
                + "    cheese(10) as b,\n"
                + "    tomato('red', t.column) as c\n"
                + "FROM\n"
                + "    table t", select.toString());
    }

    @Test
    public void usingFunctionsInMatchCriteria() {
        Table cards = new Table("credit_cards");
        TableColumn numberColumn = cards.getColumn("number");
        TableColumn issueColumn = cards.getColumn("issue");
        TableColumn issueDate = cards.getColumn("issue_date");
        TableColumn expiryDate = cards.getColumn("expiry_date");

        TableAccessor c = cards.getAccessor("c");

        SelectQuery select = new SelectQuery();

        select.addToSelection(c.getColumn(numberColumn));
        select.addToSelection(c.getColumn(issueColumn));

        select.addCriteria(new BetweenCriteria(
                new FunctionCall("getDate"), c.getColumn(issueDate), c.getColumn(expiryDate)));

        assertEquals("SELECT\n"
                + "    c.number as a,\n"
                + "    c.issue as b\n"
                + "FROM\n"
                + "    credit_cards c\n"
                + "WHERE\n"
                + "    getDate() BETWEEN c.issue_date AND c.expiry_date", select.toString());
    }

    @Test
    public void selectingFunctionThatDoesNotReferToTables() {
        SelectQuery select = new SelectQuery();
        select.addToSelection(new FunctionCall("getdate"));

        assertEquals("SELECT\n"
                + "    getdate() as a", select.toString());
    }
}
