package com.bimedia.squiggle;

import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.Test;
import static org.junit.Assert.assertThat;

public class SelectWildcardColumnTest {

    @Test
    public void selectWildcardColumn() {
        Table people = new Table("people");

        SelectQuery select = new SelectQuery();

        select.addToSelection(people.getWildcard());

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    people.* "
                + "FROM "
                + "    people"));
    }
}
