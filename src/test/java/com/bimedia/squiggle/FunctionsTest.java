package com.bimedia.squiggle;

import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.bimedia.squiggle.criteria.BetweenCriteria;
import com.bimedia.squiggle.literal.IntegerLiteral;
import com.bimedia.squiggle.literal.StringLiteral;
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
