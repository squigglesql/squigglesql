package com.bimedia.squiggle.criteria;

import com.bimedia.squiggle.Criteria;
import com.bimedia.squiggle.Matchable;
import com.bimedia.squiggle.Table;
import com.bimedia.squiggle.output.Output;

import java.util.Set;

public class IsNullCriteria implements Criteria {

    private final Matchable matched;

    public IsNullCriteria(Matchable matched) {
        this.matched = matched;
    }

    @Override
    public void write(Output out) {
        matched.write(out);
        out.print(" IS NULL");
    }

    public void addReferencedTablesTo(Set<Table> tables) {
        matched.addReferencedTablesTo(tables);
    }
}
