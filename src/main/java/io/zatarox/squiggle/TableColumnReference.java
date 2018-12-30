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

import java.util.Set;

public class TableColumnReference implements Selectable {

    private final TableColumn column;
    private final TableReference tableReference;

    TableColumnReference(TableColumn column, TableReference tableReference) {
        this.column = column;
        this.tableReference = tableReference;
    }

    public TableColumn getColumn() {
        return column;
    }

    public TableReference getTableReference() {
        return tableReference;
    }

    @Override
    public void write(Output output) {
        output.write(tableReference.getAlias()).write('.').write(column.getName());
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        tableReferences.add(tableReference);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableColumnReference that = (TableColumnReference) o;

        if (!column.equals(that.column)) return false;
        return tableReference.equals(that.tableReference);
    }

    @Override
    public int hashCode() {
        int result = column.hashCode();
        result = 31 * result + tableReference.hashCode();
        return result;
    }
}
