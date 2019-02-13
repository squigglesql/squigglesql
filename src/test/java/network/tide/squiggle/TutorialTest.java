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
package network.tide.squiggle;

import network.tide.squiggle.criteria.InCriteria;
import network.tide.squiggle.criteria.MatchCriteria;
import network.tide.squiggle.literal.Literal;
import network.tide.squiggle.query.SelectQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TutorialTest {

    @Test
    public void testTutorial() {
        // define tables
        Table order = new Table("order");
        TableColumn orderId = order.get("id");
        TableColumn orderTotalPrice = order.get("total_price");
        TableColumn orderStatus = order.get("status");
        TableColumn orderItems = order.get("items");
        TableColumn orderDelivery = order.get("delivery");
        TableColumn orderWarehouseId = order.get("warehouse_id");

        Table warehouse = new Table("warehouse");
        TableColumn warehouseId = warehouse.get("id");
        TableColumn warehouseSize = warehouse.get("size");
        TableColumn warehouseLocation = warehouse.get("location");

        Table offer = new Table("offer");
        TableColumn offerLocation = offer.get("location");
        TableColumn offerValid = offer.get("valid");

        // basic query
        TableReference o = order.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(o.get(orderId));
        select.addToSelection(o.get(orderTotalPrice));

        // matches
        select.addCriteria(new MatchCriteria(
                o.get(orderStatus), MatchCriteria.EQUALS, new TypeCast(Literal.of("processed"), "status")));
        select.addCriteria(new MatchCriteria(
                o.get(orderItems), MatchCriteria.LESS, Literal.of(5)));
        select.addCriteria(new InCriteria(o.get(orderDelivery),
                Literal.of("post"), Literal.of("fedex"), Literal.of("goat")));

        // join
        TableReference w = warehouse.refer();

        select.addCriteria(new MatchCriteria(
                o.get(orderWarehouseId), MatchCriteria.EQUALS, w.get(warehouseId)));

        // use joined table
        select.addToSelection(w.get(warehouseLocation));
        select.addCriteria(new MatchCriteria(
                w.get(warehouseSize), MatchCriteria.EQUALS, Literal.of("big")));

        // build subselect query
        TableReference f = offer.refer();

        SelectQuery subSelect = new SelectQuery();

        subSelect.addToSelection(f.get(offerLocation));
        subSelect.addCriteria(new MatchCriteria(
                f.get(offerValid), MatchCriteria.EQUALS, Literal.of(true)));

        // add subselect to original query
        select.addCriteria(new InCriteria(w.get(warehouseLocation), subSelect));

        assertEquals("SELECT\n"
                + "    o.id,\n"
                + "    o.total_price,\n"
                + "    w.location\n"
                + "FROM\n"
                + "    order o,\n"
                + "    warehouse w\n"
                + "WHERE\n"
                + "    o.status = 'processed'::status AND\n"
                + "    o.items < 5 AND\n"
                + "    o.delivery IN ('post', 'fedex', 'goat') AND\n"
                + "    o.warehouse_id = w.id AND\n"
                + "    w.size = 'big' AND\n"
                + "    w.location IN ((\n"
                + "        SELECT\n"
                + "            o.location\n"
                + "        FROM\n"
                + "            offer o\n"
                + "        WHERE\n"
                + "            o.valid = true\n"
                + "    ))", select.toString());
    }
}
