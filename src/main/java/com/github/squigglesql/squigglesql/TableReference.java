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

import com.github.squigglesql.squigglesql.alias.Aliasable;
import com.github.squigglesql.squigglesql.alias.PreferredAliases;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Table reference that you may use in a query. You can obtain a new reference by calling {@link Table#refer()} method.
 * You may create multiple references to a single table to build complex SQL queries, and every reference will obtain
 * an unique alias.
 */
public class TableReference implements Aliasable, FromItem {

    private final Table table;

    private final Map<TableColumn, TableColumnReference> columnReferenceCache = new HashMap<>();

    /**
     * Creates a table reference. Instead of constructing this object directly, you should call {@link Table#refer()}
     * method.
     *
     * @param table table to refer.
     */
    TableReference(Table table) {
        this.table = table;
    }

    /**
     * @return the referred table.
     */
    public Table getTable() {
        return table;
    }

    /**
     * Creates a new column reference that you can further use in an SQL query.
     *
     * @param column column to refer.
     * @return column reference.
     * @throws IllegalArgumentException if the column is located in a difference database table.
     */
    public TableColumnReference get(TableColumn column) {
        if (column == null) {
            throw new IllegalArgumentException("Can not create a reference to a null column.");
        }
        TableColumnReference reference = columnReferenceCache.get(column);
        if (reference == null) {
            if (!column.getTable().equals(table)) {
                throw new IllegalArgumentException(
                        "Can not create a reference to a column defined in a different database table.");
            }
            reference = new TableColumnReference(column, this);
            columnReferenceCache.put(column, reference);
        }
        return reference;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler
                .quote(table.getName(), compiler.getSyntax().getTableQuote())
                .write(' ')
                .quote(compiler.getAlias(this), compiler.getSyntax().getTableReferenceQuote());
    }

    @Override
    public Iterable<String> getPreferredAliases() {
        return new PreferredAliases(table.getName());
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        tableReferences.add(this);
    }
}
