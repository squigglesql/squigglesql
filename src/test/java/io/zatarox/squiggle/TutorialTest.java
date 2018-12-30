/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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
import io.zatarox.squiggle.literal.Literal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TutorialTest {

    @Test
    public void tutorial() {
        // define tables
        Table order = new Table("order");
        TableColumn orderId = order.getColumn("id");
        TableColumn orderTotalPrice = order.getColumn("total_price");
        TableColumn orderStatus = order.getColumn("status");
        TableColumn orderItems = order.getColumn("items");
        TableColumn orderDelivery = order.getColumn("delivery");
        TableColumn orderWarehouseId = order.getColumn("warehouse_id");

        Table warehouse = new Table("warehouse");
        TableColumn warehouseId = warehouse.getColumn("id");
        TableColumn warehouseSize = warehouse.getColumn("size");
        TableColumn warehouseLocation = warehouse.getColumn("location");

        Table offer = new Table("offer");
        TableColumn offerLocation = offer.getColumn("location");
        TableColumn offerValid = offer.getColumn("valid");

        // basic query
        TableReference o = order.createReference("o");

        SelectQuery select = new SelectQuery();

        select.addToSelection(o.getColumn(orderId));
        select.addToSelection(o.getColumn(orderTotalPrice));

        // matches
        select.addCriteria(new MatchCriteria(
                o.getColumn(orderStatus), MatchCriteria.EQUALS, Literal.of("processed")));
        select.addCriteria(new MatchCriteria(
                o.getColumn(orderItems), MatchCriteria.LESS, Literal.of(5)));
        select.addCriteria(new InCriteria(o.getColumn(orderDelivery),
                Literal.of("post"), Literal.of("fedex"), Literal.of("goat")));

        // join
        TableReference w = warehouse.createReference("w");

        select.addCriteria(new MatchCriteria(
                o.getColumn(orderWarehouseId), MatchCriteria.EQUALS, w.getColumn(warehouseId)));

        // use joined table
        select.addToSelection(w.getColumn(warehouseLocation));
        select.addCriteria(new MatchCriteria(
                w.getColumn(warehouseSize), MatchCriteria.EQUALS, Literal.of("big")));

        // build subselect query
        TableReference f = offer.createReference("f");

        SelectQuery subSelect = new SelectQuery();

        subSelect.addToSelection(f.getColumn(offerLocation));
        subSelect.addCriteria(new MatchCriteria(
                f.getColumn(offerValid), MatchCriteria.EQUALS, Literal.of(true)));

        // add subselect to original query
        select.addCriteria(new InCriteria(w.getColumn(warehouseLocation), subSelect));

        assertEquals("SELECT\n"
                + "    o.id as a,\n"
                + "    o.total_price as b,\n"
                + "    w.location as c\n"
                + "FROM\n"
                + "    order o,\n"
                + "    warehouse w\n"
                + "WHERE\n"
                + "    o.status = 'processed' AND\n"
                + "    o.items < 5 AND\n"
                + "    o.delivery IN ('post', 'fedex', 'goat') AND\n"
                + "    o.warehouse_id = w.id AND\n"
                + "    w.size = 'big' AND\n"
                + "    w.location IN ((\n"
                + "        SELECT\n"
                + "            f.location as a\n"
                + "        FROM\n"
                + "            offer f\n"
                + "        WHERE\n"
                + "            f.valid = true\n"
                + "    ))", select.toString());
    }
}
