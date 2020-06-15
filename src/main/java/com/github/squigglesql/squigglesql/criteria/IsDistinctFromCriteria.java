package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;

import java.util.Set;

public class IsDistinctFromCriteria implements Criteria {

    private final Matchable left;
    private final Matchable right;

    public IsDistinctFromCriteria(Matchable left, Matchable right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.getSyntax().compileIsDistinctFrom(compiler, left, right);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        left.collectTableReferences(tableReferences);
        right.collectTableReferences(tableReferences);
    }
}
