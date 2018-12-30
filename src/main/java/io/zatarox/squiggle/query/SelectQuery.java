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
package io.zatarox.squiggle.query;

import io.zatarox.squiggle.Matchable;
import io.zatarox.squiggle.Output;
import io.zatarox.squiggle.Outputable;
import io.zatarox.squiggle.Selectable;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.criteria.Criteria;
import io.zatarox.squiggle.util.AliasGenerator;
import io.zatarox.squiggle.util.CollectionWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectQuery extends Query implements Matchable {

    private final List<ResultColumn> selection = new ArrayList<ResultColumn>();
    private final List<Criteria> criterias = new ArrayList<Criteria>();
    private final List<Outputable> orders = new ArrayList<Outputable>();

    private final boolean distinct;

    public SelectQuery() {
        this(false);
    }

    public SelectQuery(boolean distinct) {
        this.distinct = distinct;
    }

    public ResultColumn addToSelection(Selectable selectable) {
        if (selectable == null) {
            throw new NullPointerException("Selection can not be null.");
        }
        ResultColumn resultColumn = new ResultColumn(selectable, AliasGenerator.generateAlias(selection.size()));
        selection.add(resultColumn);
        return resultColumn;
    }

    public void addCriteria(Criteria criteria) {
        if (criteria == null) {
            throw new NullPointerException("Criteria can not be null.");
        }
        this.criterias.add(criteria);
    }

    public void addOrder(ResultColumn resultColumn, boolean ascending) {
        if (resultColumn == null) {
            throw new NullPointerException("Ordering column can not be null.");
        }
        this.orders.add(new OrderByResult(resultColumn, ascending));
    }

    public void addOrder(Selectable selectable, boolean ascending) {
        if (selectable == null) {
            throw new NullPointerException("Ordering criteria can not be null.");
        }
        this.orders.add(new OrderBySelectable(selectable, ascending));
    }

    @Override
    public void write(Output output) {
        boolean nested = !output.isEmpty();
        if (nested) {
            output.writeln('(').indent();
        }

        output.write("SELECT");
        if (distinct) {
            output.write(" DISTINCT");
        }

        if (selection.isEmpty()) {
            output.writeln(" 1");
        } else {
            CollectionWriter.writeCollection(output, selection, ",", false, true);
        }

        List<TableReference> tableReferences = getTableReferences();
        if (!tableReferences.isEmpty()) {
            output.write("FROM");
            CollectionWriter.writeCollection(output, tableReferences, ",", false, true);
        }

        if (!criterias.isEmpty()) {
            output.write("WHERE");
            CollectionWriter.writeCollection(output, criterias, " AND", false, true);
        }

        if (!orders.isEmpty()) {
            output.write("ORDER BY");
            CollectionWriter.writeCollection(output, orders, ",", false, true);
        }

        if (nested) {
            output.writeln().unindent().write(')');
        }
    }

    @Override
    public void collectTableReferences(Set<TableReference> tables) {
    }

    private List<TableReference> getTableReferences() {
        Set<TableReference> tables = new HashSet<TableReference>();
        for (ResultColumn resultColumn : selection) {
            resultColumn.collectTableReferences(tables);
        }
        for (Criteria criteria : criterias) {
            criteria.collectTableReferences(tables);
        }
        List<TableReference> tableList = new ArrayList<TableReference>(tables);
        Collections.sort(tableList, new TableReference.Comparator());
        return tableList;
    }
}
