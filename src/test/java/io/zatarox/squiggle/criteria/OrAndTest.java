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
package io.zatarox.squiggle.criteria;

import io.zatarox.squiggle.Criteria;
import io.zatarox.squiggle.SelectQuery;
import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.WildCardColumn;
import io.zatarox.squiggle.criteria.AND;
import io.zatarox.squiggle.criteria.MatchCriteria;
import io.zatarox.squiggle.criteria.OR;
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
