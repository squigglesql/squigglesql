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
package io.zatarox.squiggle.criteria;

import io.zatarox.squiggle.Column;
import io.zatarox.squiggle.Criteria;
import io.zatarox.squiggle.Literal;
import io.zatarox.squiggle.LiteralValueSet;
import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.ValueSet;
import io.zatarox.squiggle.output.Output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MultiInCriteria implements Criteria {

    private final Table table;
    private final Collection<List<Literal>> valueSets;
    private final ArrayList<Column> columnList;

    public MultiInCriteria(Table table, List<String> columnNames, Collection<List<Literal>> valueSets) {
        this.table = table;
        this.valueSets = valueSets;
        this.columnList = new ArrayList<Column>();
        for (String columnName : columnNames) {
            columnList.add(table.getColumn(columnName));
        }
    }

    @Override
    public void write(Output out) {
        out.print("(");

        for (int i = 0; i < columnList.size(); i++) {
            columnList.get(i).write(out);
            if (i != columnList.size() - 1) {
                out.print(", ");
            }
        }
        out.println(")");
        out.println(" IN (");
        out.indent();
        Iterator<List<Literal>> valueSetsIterator;
        for (valueSetsIterator = valueSets.iterator(); valueSetsIterator.hasNext();) {
            out.print("( ");
            ValueSet valueSet = new LiteralValueSet(valueSetsIterator.next());
            valueSet.write(out);
            out.print(" )");
            if (valueSetsIterator.hasNext()) {
                out.print(", ");
            }
        }
        out.println();
        out.unindent();
        out.print(")");
    }

    @Override
    public void addReferencedTablesTo(Set<Table> tables) {
        for (Column col : columnList) {
            col.addReferencedTablesTo(tables);
        }
    }
}
