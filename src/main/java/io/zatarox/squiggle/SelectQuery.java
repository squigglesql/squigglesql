/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectQuery extends Query implements Matchable {

    private final List<ResultColumn> selection = new ArrayList<ResultColumn>();
    private final List<Criteria> criterias = new ArrayList<Criteria>();
    private final List<Order> orders = new ArrayList<Order>();

    private final boolean isDistinct;

    public SelectQuery() {
        this(false);
    }

    public SelectQuery(boolean isDistinct) {
        this.isDistinct = isDistinct;
    }

    public ResultColumn addToSelection(Selectable selectable) {
        ResultColumn resultColumn = new ResultColumn(selectable, AliasGenerator.generateAlias(selection.size()));
        selection.add(resultColumn);
        return resultColumn;
    }

    public void addCriteria(Criteria criteria) {
        this.criterias.add(criteria);
    }

    public void addOrder(ResultColumn resultColumn, boolean ascending) {
        this.orders.add(new Order(resultColumn, ascending));
    }

    @Override
    public boolean isNull() {
        return false; // no way to find out...
    }

    @Override
    public void write(Output output) {
        boolean nested = !output.isEmpty();
        if (nested) {
            output.writeln('(').indent();
        }

        output.write("SELECT");
        if (isDistinct) {
            output.write(" DISTINCT");
        }

        if (selection.isEmpty()) {
            output.writeln(" 1");
        } else {
            CollectionWriter.writeCollection(output, selection, ",", false, true);
        }

        List<TableAccessor> tableAccessors = findAllUsedTableAccessors();
        if (!tableAccessors.isEmpty()) {
            output.write("FROM");
            CollectionWriter.writeCollection(output, tableAccessors, ",", false, true);
        }

        // Add criteria
        if (criterias.size() > 0) {
            output.write("WHERE");
            CollectionWriter.writeCollection(output, criterias, " AND", false, true);
        }

        // Add order
        if (orders.size() > 0) {
            output.write("ORDER BY");
            CollectionWriter.writeCollection(output, orders, ",", false, true);
        }

        if (nested) {
            output.writeln().unindent().write(')');
        }
    }

    @Override
    public void addReferencedTableAccessorsTo(Set<TableAccessor> tables) {
    }

    private List<TableAccessor> findAllUsedTableAccessors() {
        Set<TableAccessor> tables = new HashSet<TableAccessor>();
        for (ResultColumn resultColumn : selection) {
            resultColumn.addReferencedTableAccessorsTo(tables);
        }
        for (Criteria criteria : criterias) {
            criteria.addReferencedTableAccessorsTo(tables);
        }
        List<TableAccessor> tableList = new ArrayList<TableAccessor>(tables);
        Collections.sort(tableList, new TableAccessor.Comparator());
        return tableList;
    }
}
