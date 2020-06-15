package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.TableReference;

import java.util.Set;

abstract class UnaryCriteria extends Criteria {

    protected final Matchable value;

    UnaryCriteria(Matchable value) {
        this.value = value;
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        value.collectTableReferences(tableReferences);
    }
}
