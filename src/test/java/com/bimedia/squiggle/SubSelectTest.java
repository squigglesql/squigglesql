package com.bimedia.squiggle;

import com.bimedia.squiggle.criteria.InCriteria;
import com.bimedia.squiggle.criteria.MatchCriteria;

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
