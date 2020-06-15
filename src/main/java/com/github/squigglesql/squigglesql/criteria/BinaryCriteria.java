package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.TableReference;

import java.util.Set;

abstract class BinaryCriteria extends Criteria {

    protected final Matchable left;
    protected final Matchable right;

    BinaryCriteria(Matchable left, Matchable right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        left.collectTableReferences(tableReferences);
        right.collectTableReferences(tableReferences);
    }
}
