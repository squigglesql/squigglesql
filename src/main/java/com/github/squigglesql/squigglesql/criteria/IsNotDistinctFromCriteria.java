package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.QueryCompiler;

class IsNotDistinctFromCriteria extends BinaryCriteria {

    IsNotDistinctFromCriteria(Matchable left, Matchable right) {
        super(left, right);
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.getSyntax().compileIsNotDistinctFrom(compiler, left, right);
    }
}
