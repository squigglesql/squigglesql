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
import io.zatarox.squiggle.QueryCompiler;
import io.zatarox.squiggle.TableColumn;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.alias.AliasGenerator;
import io.zatarox.squiggle.criteria.Criteria;
import io.zatarox.squiggle.util.CollectionWriter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UpdateQuery extends Query {

    private final TableReference tableReference;
    private final List<Assignment> assignments = new ArrayList<Assignment>();
    private final List<Criteria> criterias = new ArrayList<Criteria>();

    public UpdateQuery(TableReference tableReference) {
        if (tableReference == null) {
            throw new NullPointerException("Table reference can not be empty.");
        }
        this.tableReference = tableReference;
    }

    public void addValue(TableColumn column, Matchable value) {
        if (!column.getTable().equals(tableReference.getTable())) {
            throw new NullPointerException("Can not insert a value to a different database table.");
        }
        assignments.add(new Assignment(column, value));
    }

    public void addCriteria(Criteria criteria) {
        if (criteria == null) {
            throw new NullPointerException("Criteria can not be null.");
        }
        this.criterias.add(criteria);
    }

    @Override
    protected void compile(Output output) {
        if (assignments.isEmpty()) {
            throw new RuntimeException("No values specified for updating.");
        }

        Set<TableReference> tableReferences = findTableReferences();
        QueryCompiler queryCompiler = new QueryCompiler(output,
                AliasGenerator.generateAliases(tableReferences, TABLE_REFERENCE_ALIAS_ALPHABET));

        queryCompiler.write("UPDATE ").write(tableReference).write(" SET");
        CollectionWriter.writeCollection(queryCompiler, assignments, ",", false, true);

        tableReferences.remove(tableReference);
        if (!tableReferences.isEmpty()) {
            queryCompiler.write("FROM");
            CollectionWriter.writeCollection(queryCompiler, tableReferences, ",", false, true);
        }

        if (criterias.size() > 0) {
            queryCompiler.write("WHERE");
            CollectionWriter.writeCollection(queryCompiler, criterias, " AND", false, true);
        }
    }

    private Set<TableReference> findTableReferences() {
        Set<TableReference> tables = new LinkedHashSet<TableReference>();
        for (Criteria criteria : criterias) {
            criteria.collectTableReferences(tables);
        }
        return tables;
    }
}
