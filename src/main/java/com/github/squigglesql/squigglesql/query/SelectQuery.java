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

import com.github.squigglesql.squigglesql.*;
import com.github.squigglesql.squigglesql.alias.AliasGenerator;
import com.github.squigglesql.squigglesql.alias.Alphabet;
import com.github.squigglesql.squigglesql.criteria.Criteria;
import com.github.squigglesql.squigglesql.statement.StatementBuilder;
import com.github.squigglesql.squigglesql.statement.StatementCompiler;
import com.github.squigglesql.squigglesql.util.CollectionWriter;

import java.sql.SQLException;
import java.util.*;

/**
 * SQL selection query.
 */
public class SelectQuery extends Query implements Matchable {

    private static final Alphabet RESULT_COLUMN_ALIAS_ALPHABET = new Alphabet('a', 19);

    private final List<ResultColumn> selection = new ArrayList<>();
    private final List<FromItem> fromItems = new ArrayList<>();
    private final List<Criteria> criterias = new ArrayList<>();
    private final List<BaseOrder> orders = new ArrayList<>();

    private final boolean distinct;

    /**
     * Creates a selection query.
     */
    public SelectQuery() {
        this(false);
    }

    /**
     * Creates a selection query.
     *
     * @param distinct adds "DISTINCT" modifier to the query.
     */
    public SelectQuery(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * Adds a column to the selection.
     *
     * @param selectable expression to assign as a column value.
     * @return result column allowing to order the query or read the {@link java.sql.ResultSet}.
     */
    public ResultColumn addToSelection(Selectable selectable) {
        if (selectable == null) {
            throw new IllegalArgumentException("Selection can not be null.");
        }
        ResultColumn resultColumn = new ResultColumn(selectable, selection.size() + 1);
        selection.add(resultColumn);
        return resultColumn;
    }

    /**
     * Adds an item to "FROM" section of the query. See {@link FromItem} subclasses for available options.
     * <p>
     * You don't have to always add from-items to a query - in the majority of cases, the query can automatically infer
     * them from its selection columns and criteria.
     *
     * @param fromItem from item to add.
     * @since 4.1.0
     */
    public void addFrom(FromItem fromItem) {
        if (fromItem == null) {
            throw new IllegalArgumentException("From item can not be null.");
        }
        fromItems.add(fromItem);
    }

    /**
     * Adds a criteria to "WHERE" section of the query. Criterias are joined with "AND" operator.
     *
     * @param criteria criteria to add.
     */
    public void addCriteria(Criteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Criteria can not be null.");
        }
        this.criterias.add(criteria);
    }

    /**
     * Adds a result column to "ORDER BY" section of the query.
     *
     * @param resultColumn result column.
     * @param ascending    true for "ASC" ordering, false for "DESC".
     */
    public void addOrder(ResultColumn resultColumn, boolean ascending) {
        if (resultColumn == null) {
            throw new IllegalArgumentException("Ordering column can not be null.");
        }
        this.orders.add(new OrderByResult(resultColumn, ascending));
    }

    /**
     * Adds an expression to "ORDER BY" section of the query.
     *
     * @param selectable expression.
     * @param ascending  true for "ASC" ordering, false for "DESC".
     */
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
        Set<TableReference> mentionedTableReferences = findMentionedTableReferences();
        Set<TableReference> usedTableReferences = findUsedTableReferences();
        Set<TableReference> missingTableReferences = new LinkedHashSet<>(usedTableReferences);
        missingTableReferences.removeAll(mentionedTableReferences);

        Set<FromItem> allFromItems = new LinkedHashSet<>(fromItems);
        allFromItems.addAll(missingTableReferences);

        Set<ResultColumn> resultReferences = findResultReferences();
        QueryCompiler compiler = new QueryCompiler(output,
                AliasGenerator.generateAliases(usedTableReferences, TABLE_REFERENCE_ALIAS_ALPHABET),
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

        if (!allFromItems.isEmpty()) {
            compiler.write("FROM");
            CollectionWriter.writeCollection(compiler, allFromItems, ",", false, true);
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

    @Override
    protected <S> StatementBuilder<S> createStatementBuilder(StatementCompiler<S> compiler, String query)
            throws SQLException {
        return compiler.createStatementBuilder(query);
    }

    private Set<TableReference> findMentionedTableReferences() {
        Set<TableReference> references = new LinkedHashSet<>();
        for (FromItem source : fromItems) {
            source.collectTableReferences(references);
        }
        return references;
    }

    private Set<TableReference> findUsedTableReferences() {
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
