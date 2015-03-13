package com.bimedia.squiggle.criteria;

import com.bimedia.squiggle.Column;
import com.bimedia.squiggle.Criteria;
import com.bimedia.squiggle.Literal;
import com.bimedia.squiggle.LiteralValueSet;
import com.bimedia.squiggle.Table;
import com.bimedia.squiggle.ValueSet;
import com.bimedia.squiggle.output.Output;

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
