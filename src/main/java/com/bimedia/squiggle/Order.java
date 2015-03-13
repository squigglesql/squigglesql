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
package com.bimedia.squiggle;

import java.util.Set;

import com.bimedia.squiggle.output.Output;
import com.bimedia.squiggle.output.Outputable;

/**
 * ORDER BY clause. See SelectQuery.addOrder(Order).
 */
public class Order implements Outputable {

    public static final boolean ASCENDING = true;
    public static final boolean DESCENDING = false;

    private final Column column;
    private final boolean ascending;

    /**
     * @param column Column to order by.
     * @param ascending Order.ASCENDING or Order.DESCENDING
     */
    public Order(Column column, boolean ascending) {
        this.column = column;
        this.ascending = ascending;
    }

    public Projection getColumn() {
        return column;
    }

    public void write(Output out) {
        column.write(out);
        if (!ascending) {
            out.print(" DESC");
        }
    }

    public void addReferencedTablesTo(Set<Table> tables) {
        column.addReferencedTablesTo(tables);
    }
}
