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

import com.zatarox.squiggle.FunctionCall;
import com.zatarox.squiggle.SelectQuery;
import com.zatarox.squiggle.Table;
import com.zatarox.squiggle.criteria.BetweenCriteria;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.zatarox.squiggle.literal.IntegerLiteral;
import com.zatarox.squiggle.literal.StringLiteral;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;

public class FunctionsTest {

    @Test
    public void functions() {
        SelectQuery select = new SelectQuery();

        Table table = new Table("t");

        select.addToSelection(new FunctionCall("sheep"));
        select.addToSelection(new FunctionCall("cheese", new IntegerLiteral(10)));
        select.addToSelection(new FunctionCall("tomato", new StringLiteral("red"), table.getColumn("c")));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    sheep() , "
                + "    cheese(10) , "
                + "    tomato('red', t.c) "
                + "FROM "
                + "    t "));

    }

    @Test
    public void usingFunctionsInMatchCriteria() {
        Table cards = new Table("credit_cards");

        SelectQuery select = new SelectQuery();

        select.addToSelection(cards.getColumn("number"));
        select.addToSelection(cards.getColumn("issue"));

        select.addCriteria(
                new BetweenCriteria(new FunctionCall("getDate"),
                        cards.getColumn("issue_date"), cards.getColumn("expiry_date")));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    credit_cards.number , "
                + "    credit_cards.issue "
                + "FROM "
                + "    credit_cards "
                + "WHERE "
                + "    getDate() BETWEEN credit_cards.issue_date AND credit_cards.expiry_date"));
    }

    @Test
    public void selectingFunctionThatDoesNotReferToTables() {
        SelectQuery select = new SelectQuery();
        select.addToSelection(new FunctionCall("getdate"));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT"
                + "    getdate()"));
    }
}
