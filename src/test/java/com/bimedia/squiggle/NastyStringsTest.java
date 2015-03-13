package com.bimedia.squiggle;

import com.bimedia.squiggle.criteria.MatchCriteria;

import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class NastyStringsTest {

    @Test
    public void testNastyStrings() {
        Table people = new Table("people");

        SelectQuery select = new SelectQuery();

        select.addColumn(people, "firstname");

        select.addCriteria(
                new MatchCriteria(people, "foo", MatchCriteria.GREATER, "I've got a quote"));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT\n"
                + "    people.firstname\n"
                + "FROM\n"
                + "    people\n"
                + "WHERE\n"
                + "    people.foo > 'I\\'ve got a quote'"));
    }
}
