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
package io.zatarox.squiggle;

import io.zatarox.squiggle.criteria.InCriteria;
import io.zatarox.squiggle.criteria.MatchCriteria;

import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.Test;
import static org.junit.Assert.assertThat;

public class TutorialTest {

    @Test
    public void tutorial() {
        // basic query
        SelectQuery select = new SelectQuery();

        // add columns
        Table orders = new Table("orders_table");
        select.addColumn(orders, "id");
        select.addColumn(orders, "total_price");

        // matches
        select.addCriteria(new MatchCriteria(orders, "status", MatchCriteria.EQUALS, "processed"));
        select.addCriteria(new MatchCriteria(orders, "items", MatchCriteria.LESS, 5));

        // IN...
        select.addCriteria(new InCriteria(orders, "delivery",
                new String[]{"post", "fedex", "goat"}));

        // join
        Table warehouses = new Table("warehouses_table");
        select.addJoin(orders, "warehouse_id", warehouses, "id");

        // use joined table
        select.addColumn(warehouses, "location");
        select.addCriteria(new MatchCriteria(warehouses, "size", MatchCriteria.EQUALS, "big"));

        // build subselect query
        SelectQuery subSelect = new SelectQuery();
        Table offers = new Table("offers_table");
        subSelect.addColumn(offers, "location");
        subSelect.addCriteria(new MatchCriteria(offers, "valid", MatchCriteria.EQUALS, true));

        // add subselect to original query
        select.addCriteria(new InCriteria(warehouses, "location", subSelect));

        assertThat(select.toString(), IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
                "SELECT "
                + "    orders_table.id , "
                + "    orders_table.total_price , "
                + "    warehouses_table.location  "
                + "FROM "
                + "    orders_table , "
                + "    warehouses_table  "
                + "WHERE "
                + "    orders_table.status = 'processed' AND "
                + "    orders_table.items < 5 AND "
                + "    orders_table.delivery IN ( "
                + "        'post', 'fedex', 'goat' "
                + "    ) AND "
                + "    orders_table.warehouse_id = warehouses_table.id AND "
                + "    warehouses_table.size = 'big' AND "
                + "    warehouses_table.location IN ( "
                + "        SELECT "
                + "            offers_table.location  "
                + "        FROM "
                + "            offers_table  "
                + "        WHERE "
                + "            offers_table.valid = true  "
                + "    )"));
    }
}
