package com.github.squigglesql.squigglesql.join;

import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.TableReference;

import java.util.Set;

public abstract class Join implements FromItem {

    protected final FromItem left;
    protected final TableReference right;

    protected Join(FromItem left, TableReference right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public abstract void compile(QueryCompiler compiler);

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        left.collectTableReferences(tableReferences);
        tableReferences.add(right);
    }
}
