package com.bimedia.squiggle.criteria;

import java.util.Set;

import com.bimedia.squiggle.Criteria;
import com.bimedia.squiggle.Matchable;
import com.bimedia.squiggle.Table;
import com.bimedia.squiggle.output.Output;

public class IsNotNullCriteria implements Criteria {

    private final Matchable matched;

    public IsNotNullCriteria(Matchable matched) {
        this.matched = matched;
    }

    @Override
    public void write(Output out) {
        matched.write(out);
        out.print(" IS NOT NULL");
    }

    public void addReferencedTablesTo(Set<Table> tables) {
        matched.addReferencedTablesTo(tables);
    }
}
