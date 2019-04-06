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
package com.github.squigglesql.squigglesql.query;

import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.Output;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.util.CollectionWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL single row insertion query.
 */
public class InsertQuery extends Query {

    private final Table table;
    private final List<TableColumn> columns = new ArrayList<>();
    private final List<Matchable> values = new ArrayList<>();

    /**
     * Creates an insertion query.
     * @param table table to insert a row to.
     */
    public InsertQuery(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("Table can not be empty.");
        }
        this.table = table;
    }

    /**
     * Adds a column value to the query.
     * @param column table column.
     * @param value value to add to the column.
     */
    public void addValue(TableColumn column, Matchable value) {
        if (!column.getTable().equals(table)) {
            throw new IllegalArgumentException("Can not insert a value to a different database table.");
        }
        columns.add(column);
        values.add(value);
    }

    @Override
    protected void compile(Output output) {
        if (columns.isEmpty()) {
            throw new IllegalStateException("No values specified for insertion.");
        }

        QueryCompiler compiler = new QueryCompiler(output);

        compiler.write("INSERT INTO ").quote(table.getName(), output.getSyntax().getTableQuote());
        CollectionWriter.writeCollection(compiler, columns, ", ", true, false);

        compiler.write(" VALUES ");
        CollectionWriter.writeCollection(compiler, values, ", ", true, false);
    }
}
