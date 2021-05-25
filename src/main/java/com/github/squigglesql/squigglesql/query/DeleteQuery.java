/*
 * Copyright 2021 Cody Ebberson.
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

import com.github.squigglesql.squigglesql.Output;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.alias.AliasGenerator;
import com.github.squigglesql.squigglesql.criteria.Criteria;
import com.github.squigglesql.squigglesql.statement.StatementBuilder;
import com.github.squigglesql.squigglesql.statement.StatementCompiler;
import com.github.squigglesql.squigglesql.util.CollectionWriter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * SQL delete query.
 *
 * @since 4.2.0
 */
public class DeleteQuery extends Query {
    
    private final TableReference tableReference;
    private final List<Criteria> criterias = new ArrayList<>();

    /**
     * Creates a delete query.
     *
     * @param tableReference table reference to delete rows from.
     */
    public DeleteQuery(TableReference tableReference) {
        if (tableReference == null) {
            throw new IllegalArgumentException("Table reference can not be empty.");
        }
        this.tableReference = tableReference;
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

    @Override
    protected void compile(Output output) {
        Set<TableReference> tableReferences = findTableReferences();

        if (tableReferences.size() > 1) {
            throw new IllegalArgumentException("Cannot delete using multiple tables.");
        }

        QueryCompiler queryCompiler = new QueryCompiler(output,
                AliasGenerator.generateAliases(tableReferences, TABLE_REFERENCE_ALIAS_ALPHABET));

        output.getSyntax().compileDeleteFrom(queryCompiler, tableReference);

        if (criterias.size() > 0) {
            queryCompiler.write("WHERE");
            CollectionWriter.writeCollection(queryCompiler, criterias, " AND", false, true);
        }
    }

    @Override
    protected <S> StatementBuilder<S> createStatementBuilder(StatementCompiler<S> compiler, String query)
            throws SQLException {
        return compiler.createStatementBuilder(query);
    }

    private Set<TableReference> findTableReferences() {
        Set<TableReference> tables = new LinkedHashSet<>();
        tables.add(tableReference);
        for (Criteria criteria : criterias) {
            criteria.collectTableReferences(tables);
        }
        return tables;
    }
}
