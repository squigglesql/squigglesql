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
import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.output.Output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MultiAndCriteria implements Criteria {

    private final List<Column> columnList;
    private final Collection<List<Literal>> valueLists;

    public MultiAndCriteria(Table table, List<String> columnNames, Collection<List<Literal>> values) {
        this.columnList = new ArrayList<Column>();
        for (String columnName : columnNames) {
            columnList.add(table.getColumn(columnName));
        }
        this.valueLists = values;
    }

    @Override
    public void write(Output out) {
        if (!valueLists.isEmpty()) {
            List<Criteria> orClauses = new ArrayList<Criteria>();

            for (List<Literal> values : valueLists) {
                List<Criteria> orClause = new ArrayList<Criteria>();
                for (int i = 0; i < values.size(); i++) {
                    orClause.add(new MatchCriteria(columnList.get(i), MatchCriteria.EQUALS, values.get(i)));
                }

                orClauses.add(new CriteriaExpression(orClause, CriteriaExpression.Operator.AND));
            }

            Criteria finalCriteria = new CriteriaExpression(orClauses, CriteriaExpression.Operator.OR);

            finalCriteria.write(out);
        } else {
            out.println(" ( ");
            for (int i = 0; i < columnList.size(); i++) {
                columnList.get(i).write(out);
                if (i != columnList.size() - 1) {
                    out.print(", ");
                }
            }
            out.println(" ) IN () ");
        }
    }

    @Override
    public void addReferencedTablesTo(Set<Table> tables) {
        for (Column col : columnList) {
            col.addReferencedTablesTo(tables);
        }
    }
}
