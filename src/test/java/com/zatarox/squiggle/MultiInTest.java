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

import com.zatarox.squiggle.Literal;
import com.zatarox.squiggle.SelectQuery;
import com.zatarox.squiggle.Table;
import com.zatarox.squiggle.WildCardColumn;
import com.zatarox.squiggle.criteria.MultiInCriteria;
import com.zatarox.squiggle.literal.StringLiteral;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;

import static org.junit.Assert.assertThat;

public class MultiInTest {

    @Test
    public void multiIn() {
        Table user = new Table("user");

        SelectQuery select = new SelectQuery();

        select.addToSelection(new WildCardColumn(user));

        List<String> columns = new ArrayList<String>();
        Collection<List<Literal>> values = new ArrayList<List<Literal>>();
        columns.add("first_column");
        columns.add("second_column");
        columns.add("third_column");

        List<Literal> firstValueTuple = new ArrayList<Literal>();
        List<Literal> secondValueTuple = new ArrayList<Literal>();

        firstValueTuple.add(new StringLiteral("first_value"));
        firstValueTuple.add(new StringLiteral("second_value"));
        firstValueTuple.add(new StringLiteral("third_value"));

        secondValueTuple.add(new StringLiteral("first_value"));
        secondValueTuple.add(new StringLiteral("second_value"));
        secondValueTuple.add(new StringLiteral("third_value"));

        values.add(firstValueTuple);
        values.add(secondValueTuple);

        select.addCriteria(new MultiInCriteria(user, columns, values));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    user.* "
                + "FROM "
                + "    user "
                + "WHERE "
                + " (user.first_column, user.second_column, user.third_column) "
                + "IN ( "
                + "( 'first_value', 'second_value', 'third_value' ), ( 'first_value', 'second_value', 'third_value' )"
                + " )"));

    }
}
