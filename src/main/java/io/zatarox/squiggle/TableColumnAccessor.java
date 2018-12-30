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

public class TableColumnAccessor implements Selectable {

    private final TableColumn column;
    private final TableAccessor tableAccessor;

    TableColumnAccessor(TableColumn column, TableAccessor tableAccessor) {
        this.column = column;
        this.tableAccessor = tableAccessor;
    }

    public TableColumn getColumn() {
        return column;
    }

    public TableAccessor getTableAccessor() {
        return tableAccessor;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public void write(Output output) {
        output.write(tableAccessor.getAlias()).write('.').write(column.getName());
    }

    @Override
    public void addReferencedTableAccessorsTo(Set<TableAccessor> tableAccessors) {
        tableAccessors.add(tableAccessor);
    }
}
