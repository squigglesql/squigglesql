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
package com.github.squigglesql.squigglesql;

import java.util.Set;

/**
 * Table column reference that you may use in a query. You can obtain a new reference by calling
 * {@link TableReference#get(TableColumn)} method.
 */
public class TableColumnReference implements Selectable {

    private final TableColumn column;
    private final TableReference tableReference;

    /**
     * Creates a column reference. Instead of constructing this object directly, you should call
     * {@link TableReference#get(TableColumn)} method.
     *
     * @param column         column to refer.
     * @param tableReference table reference to use.
     */
    TableColumnReference(TableColumn column, TableReference tableReference) {
        this.column = column;
        this.tableReference = tableReference;
    }

    /**
     * @return the referred column.
     */
    public TableColumn getColumn() {
        return column;
    }

    /**
     * @return the matching table reference.
     */
    public TableReference getTableReference() {
        return tableReference;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler
                .quote(compiler.getAlias(tableReference), compiler.getSyntax().getTableReferenceQuote())
                .write('.')
                .quote(column.getName(), compiler.getSyntax().getColumnQuote());
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        tableReferences.add(tableReference);
    }
}
