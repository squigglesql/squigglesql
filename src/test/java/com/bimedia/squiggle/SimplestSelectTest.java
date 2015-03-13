package com.bimedia.squiggle;

import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.hamcrest.text.IsEqualIgnoringWhiteSpace;

public class SimplestSelectTest {

    @Test
    public void simpleSelect() {
        Table people = new Table("people");

        SelectQuery select = new SelectQuery();

        select.addColumn(people, "firstname");
        select.addColumn(people, "lastname");

        select.addOrder(people, "age", Order.DESCENDING);

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT  people.firstname , people.lastname "
                + "FROM people "
                + "ORDER BY people.age DESC"));
    }
}
