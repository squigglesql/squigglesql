package com.bimedia.squiggle;

import com.bimedia.squiggle.criteria.MatchCriteria;
import org.junit.Test;

import static com.bimedia.squiggle.criteria.MatchCriteria.GREATER;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import static org.junit.Assert.assertThat;

public class JoinTest {

    @Test
    public void joinOnForeignKeyRelationship() {
        Table people = new Table("people");
        Table departments = new Table("departments");

        SelectQuery select = new SelectQuery(); // base table

        select.addColumn(people, "firstname");
        select.addColumn(departments, "director");

        select.addJoin(people, "departmentID", departments, "id");

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    people.firstname , "
                + "    departments.director "
                + "FROM "
                + "    people , "
                + "    departments "
                + "WHERE "
                + "    people.departmentID = departments.id"));
    }

    @Test
    public void joinOnComparison() {
        Table invoices = new Table("invoices");
        Table taxPaymentDate = new Table("tax_payment_date");

        SelectQuery select = new SelectQuery(); // base table

        select.addColumn(invoices, "number");

        select.addJoin(invoices, "date", MatchCriteria.GREATER, taxPaymentDate, "date");

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    invoices.number "
                + "FROM "
                + "    invoices , "
                + "    tax_payment_date "
                + "WHERE "
                + "    invoices.date > tax_payment_date.date"));
    }

    @Test
    public void doNotHaveToExplicitlyJoinTables() {
        Table invoices = new Table("invoices");
        Table taxPaymentDate = new Table("tax_payment_date");

        SelectQuery select = new SelectQuery(); // base table

        select.addColumn(invoices, "number");
        select.addCriteria(new MatchCriteria(invoices.getColumn("date"), GREATER, taxPaymentDate.getColumn("date")));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    invoices.number "
                + "FROM "
                + "    invoices , "
                + "    tax_payment_date "
                + "WHERE "
                + "    invoices.date > tax_payment_date.date"));
    }
}
