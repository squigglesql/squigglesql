package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;

import java.util.Set;

class LiteralCriteria extends Criteria {

    static final Criteria TRUE = new LiteralCriteria("1 = 1");
    static final Criteria FALSE = new LiteralCriteria("0 = 1");

    private final String literal;

    private LiteralCriteria(String literal) {
        this.literal = literal;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(literal);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
    }
}
