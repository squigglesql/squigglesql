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
package com.github.squigglesql.squigglesql.query;

import com.github.squigglesql.squigglesql.BaseOrder;
import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.Output;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.Selectable;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.alias.AliasGenerator;
import com.github.squigglesql.squigglesql.alias.Alphabet;
import com.github.squigglesql.squigglesql.criteria.Criteria;
import com.github.squigglesql.squigglesql.util.CollectionWriter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SelectQuery extends Query implements Matchable {

    private static final Alphabet RESULT_COLUMN_ALIAS_ALPHABET = new Alphabet('a', 19);

    private final List<ResultColumn> selection = new ArrayList<>();
    private final List<Criteria> criterias = new ArrayList<>();
    private final List<BaseOrder> orders = new ArrayList<>();

    private final boolean distinct;

    public SelectQuery() {
        this(false);
    }

    public SelectQuery(boolean distinct) {
        this.distinct = distinct;
    }

    public ResultColumn addToSelection(Selectable selectable) {
        if (selectable == null) {
            throw new IllegalArgumentException("Selection can not be null.");
        }
        ResultColumn resultColumn = new ResultColumn(selectable, selection.size() + 1);
        selection.add(resultColumn);
        return resultColumn;
    }

    public void addCriteria(Criteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Criteria can not be null.");
        }
        this.criterias.add(criteria);
    }

    public void addOrder(ResultColumn resultColumn, boolean ascending) {
        if (resultColumn == null) {
            throw new IllegalArgumentException("Ordering column can not be null.");
        }
        this.orders.add(new OrderByResult(resultColumn, ascending));
    }

    public void addOrder(Selectable selectable, boolean ascending) {
        if (selectable == null) {
            throw new IllegalArgumentException("Ordering criteria can not be null.");
        }
        this.orders.add(new OrderBySelectable(selectable, ascending));
    }

    @Override
    public void collectTableReferences(Set<TableReference> tables) {
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.writeln('(').indent();
        compile(compiler.getOutput());
        compiler.writeln().unindent().write(')');
    }

    @Override
    protected void compile(Output output) {
        Set<TableReference> tableReferences = findTableReferences();
        Set<ResultColumn> resultReferences = findResultReferences();
        QueryCompiler compiler = new QueryCompiler(output,
                AliasGenerator.generateAliases(tableReferences, TABLE_REFERENCE_ALIAS_ALPHABET),
                AliasGenerator.generateAliases(resultReferences, RESULT_COLUMN_ALIAS_ALPHABET));

        compiler.write("SELECT");
        if (distinct) {
            compiler.write(" DISTINCT");
        }

        if (selection.isEmpty()) {
            compiler.writeln(" 1");
        } else {
            CollectionWriter.writeCollection(compiler, selection, ",", false, true);
        }

        if (!tableReferences.isEmpty()) {
            compiler.write("FROM");
            CollectionWriter.writeCollection(compiler, tableReferences, ",", false, true);
        }

        if (!criterias.isEmpty()) {
            compiler.write("WHERE");
            CollectionWriter.writeCollection(compiler, criterias, " AND", false, true);
        }

        if (!orders.isEmpty()) {
            compiler.write("ORDER BY");
            CollectionWriter.writeCollection(compiler, orders, ",", false, true);
        }
    }

    private Set<TableReference> findTableReferences() {
        Set<TableReference> references = new LinkedHashSet<>();
        for (ResultColumn resultColumn : selection) {
            resultColumn.collectTableReferences(references);
        }
        for (Criteria criteria : criterias) {
            criteria.collectTableReferences(references);
        }
        return references;
    }

    private Set<ResultColumn> findResultReferences() {
        Set<ResultColumn> references = new LinkedHashSet<>();
        for (BaseOrder order : orders) {
            order.collectResultReferences(references);
        }
        return references;
    }
}
