package com.bimedia.squiggle.criteria;

import com.bimedia.squiggle.Column;
import com.bimedia.squiggle.Criteria;
import com.bimedia.squiggle.Literal;
import com.bimedia.squiggle.Table;
import com.bimedia.squiggle.output.Output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MultiAndCriteria implements Criteria {

    private final Table table;
    private List<Column> columnList;
    private final Collection<List<Literal>> valueLists;

    public MultiAndCriteria(Table table, List<String> columnNames, Collection<List<Literal>> values) {
        this.table = table;
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
