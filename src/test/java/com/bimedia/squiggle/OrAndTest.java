package com.bimedia.squiggle;

import com.bimedia.squiggle.criteria.AND;
import com.bimedia.squiggle.criteria.MatchCriteria;
import com.bimedia.squiggle.criteria.OR;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;

import org.junit.Test;
import static org.junit.Assert.assertThat;

public class OrAndTest {

    @Test
    public void orAnd() {
        Table user = new Table("user");

        SelectQuery select = new SelectQuery();

        select.addToSelection(new WildCardColumn(user));

        Criteria name = new MatchCriteria(user, "name", MatchCriteria.LIKE, "a%");
        Criteria id = new MatchCriteria(user, "id", MatchCriteria.EQUALS, 12345);
        Criteria feet = new MatchCriteria(user, "feet", MatchCriteria.EQUALS, "smelly");

        select.addCriteria(new OR(name, new AND(id, feet)));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    user.* "
                + "FROM "
                + "    user "
                + "WHERE "
                + "    ( user.name LIKE 'a%' OR ( user.id = 12345 AND user.feet = 'smelly' ) )"));
    }
}
