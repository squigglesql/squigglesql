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
import io.zatarox.squiggle.TableColumn;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.criteria.Criteria;
import io.zatarox.squiggle.util.CollectionWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
    protected void write(Output output) {
        if (assignments.isEmpty()) {
            throw new RuntimeException("No values specified for updating.");
        }
        output.write("UPDATE ").write(tableReference).write(" SET");
        CollectionWriter.writeCollection(output, assignments, ",", false, true);

        List<TableReference> tableReferences = getTableReferences();
        if (!tableReferences.isEmpty()) {
            output.write("FROM");
            CollectionWriter.writeCollection(output, tableReferences, ",", false, true);
        }

        if (criterias.size() > 0) {
            output.write("WHERE");
            CollectionWriter.writeCollection(output, criterias, " AND", false, true);
        }
    }

    private List<TableReference> getTableReferences() {
        Set<TableReference> tables = new HashSet<TableReference>();
        for (Criteria criteria : criterias) {
            criteria.collectTableReferences(tables);
        }
        tables.remove(tableReference);
        List<TableReference> tableList = new ArrayList<TableReference>(tables);
        Collections.sort(tableList, new TableReference.Comparator());
        return tableList;
    }
}
