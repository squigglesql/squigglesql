/*
 * Copyright 2019 Egor Nepomnyaschih.
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

import java.util.HashMap;
import java.util.Map;

public class TableAccessor implements Outputable {

    private final Table table;
    private final String alias;

    private final Map<TableColumn, TableColumnAccessor> columnAccessorCache = new HashMap<TableColumn, TableColumnAccessor>();

    TableAccessor(Table table, String alias) {
        this.table = table;
        this.alias = alias;
    }

    public Table getTable() {
        return table;
    }

    public String getAlias() {
        return alias;
    }

    public TableColumnAccessor getColumn(TableColumn column) {
        TableColumnAccessor accessor = columnAccessorCache.get(column);
        if (accessor == null) {
            if (!column.getTable().equals(table)) {
                throw new RuntimeException("The specified column is defined in a different database table.");
            }
            accessor = new TableColumnAccessor(column, this);
            columnAccessorCache.put(column, accessor);
        }
        return accessor;
    }

    @Override
    public void write(Output output) {
        output.write(table.getName()).write(" ").write(alias);
    }

    public static class Comparator implements java.util.Comparator<TableAccessor> {

        @Override
        public int compare(TableAccessor o1, TableAccessor o2) {
            return o1.alias.compareTo(o2.alias);
        }
    }
}
