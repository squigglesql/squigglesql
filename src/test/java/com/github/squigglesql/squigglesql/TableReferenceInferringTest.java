/*
 * Copyright 2019-2020 Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.criteria.MatchCriteria;
import com.github.squigglesql.squigglesql.join.QualifiedJoin;
import com.github.squigglesql.squigglesql.join.QualifiedJoinKind;
import com.github.squigglesql.squigglesql.literal.Literal;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import org.junit.Test;

import static com.github.squigglesql.squigglesql.criteria.MatchCriteria.EQUALS;
import static org.junit.Assert.assertEquals;

public class TableReferenceInferringTest {

    @Test
    public void testInferring() {
        Table person = new Table("person");
        TableColumn personId = person.get("id");
        TableColumn personName = person.get("name");

        Table color = new Table("color");
        TableColumn colorId = color.get("id");
        TableColumn colorName = color.get("name");

        Table vehicle = new Table("vehicle");
        TableColumn vehicleName = vehicle.get("name");
        TableColumn vehiclePersonId = vehicle.get("person_id");
        TableColumn vehicleColorId = vehicle.get("color_id");

        TableReference p = person.refer();
        TableReference c = color.refer();
        TableReference v = vehicle.refer();

        // get all green vehicles, incl. their owners if any
        SelectQuery select = new SelectQuery();
        select.addToSelection(v.get(vehicleName));
        select.addToSelection(p.get(personName));

        // here we don't mention colors, but they must be inferred from criterias
        select.addFrom(new QualifiedJoin(v, QualifiedJoinKind.LEFT, p,
                new MatchCriteria(v.get(vehiclePersonId), EQUALS, p.get(personId))));

        select.addCriteria(new MatchCriteria(v.get(vehicleColorId), EQUALS, c.get(colorId)));
        select.addCriteria(new MatchCriteria(c.get(colorName), EQUALS, Literal.of("blue")));

        assertEquals("SELECT\n"
                + "    v.name,\n"
                + "    p.name\n"
                + "FROM\n"
                + "    vehicle v\n"
                + "    LEFT JOIN person p ON v.person_id = p.id,\n"
                + "    color c\n"
                + "WHERE\n"
                + "    v.color_id = c.id AND\n"
                + "    c.name = 'blue'",select.toString());
    }
}