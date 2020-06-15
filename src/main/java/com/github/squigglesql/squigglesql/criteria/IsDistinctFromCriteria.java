package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.QueryCompiler;

class IsDistinctFromCriteria extends BinaryCriteria {

    IsDistinctFromCriteria(Matchable left, Matchable right) {
        super(left, right);
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.getSyntax().compileIsDistinctFrom(compiler, left, right);
    }
}
