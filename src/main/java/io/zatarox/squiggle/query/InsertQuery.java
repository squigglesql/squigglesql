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
package io.zatarox.squiggle.query;

import io.zatarox.squiggle.Matchable;
import io.zatarox.squiggle.Output;
import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.TableColumn;
import io.zatarox.squiggle.util.CollectionWriter;

import java.util.ArrayList;
import java.util.List;

// TODO: Implement MultiInsertQuery
public class InsertQuery extends Query {

    private final Table table;
    private final List<TableColumn> columns = new ArrayList<TableColumn>();
    private final List<Matchable> values = new ArrayList<Matchable>();

    public InsertQuery(Table table) {
        if (table == null) {
            throw new NullPointerException("Table can not be empty.");
        }
        this.table = table;
    }

    public void addValue(TableColumn column, Matchable value) {
        if (!column.getTable().equals(table)) {
            throw new NullPointerException("Can not insert a value to a different database table.");
        }
        columns.add(column);
        values.add(value);
    }

    @Override
    protected void write(Output output) {
        if (columns.isEmpty()) {
            throw new RuntimeException("No values specified for insertion.");
        }
        output.write("INSERT INTO ").write(table.getName());
        CollectionWriter.writeCollection(output, columns, ", ", true, false);

        output.write(" VALUES ");
        CollectionWriter.writeCollection(output, values, ", ", true, false);
    }
}