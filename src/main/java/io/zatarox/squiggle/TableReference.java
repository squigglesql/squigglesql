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

public class TableReference implements Outputable {

    private final Table table;
    private final String alias;

    private final Map<TableColumn, TableColumnReference> columnReferenceCache = new HashMap<TableColumn, TableColumnReference>();

    TableReference(Table table, String alias) {
        this.table = table;
        this.alias = alias;
    }

    public Table getTable() {
        return table;
    }

    public String getAlias() {
        return alias;
    }

    public TableColumnReference getColumn(TableColumn column) {
        if (column == null) {
            throw new RuntimeException("Can not create a reference to a null column.");
        }
        TableColumnReference reference = columnReferenceCache.get(column);
        if (reference == null) {
            if (!column.getTable().equals(table)) {
                throw new RuntimeException(
                        "Can not create a reference to a column defined in a different database table.");
            }
            reference = new TableColumnReference(column, this);
            columnReferenceCache.put(column, reference);
        }
        return reference;
    }

    @Override
    public void write(Output output) {
        output.write(table.getName()).write(" ").write(alias);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableReference that = (TableReference) o;

        return table.equals(that.table) && alias.equals(that.alias);
    }

    @Override
    public int hashCode() {
        int result = table.hashCode();
        result = 31 * result + alias.hashCode();
        return result;
    }

    public static class Comparator implements java.util.Comparator<TableReference> {

        @Override
        public int compare(TableReference o1, TableReference o2) {
            return o1.alias.compareTo(o2.alias);
        }
    }
}
