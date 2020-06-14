package com.github.squigglesql.squigglesql.join;

import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.TableReference;

import java.util.Set;

/**
 * Abstract join.
 *
 * @since 4.1.0
 */
public abstract class Join implements FromItem {

    /**
     * Left item of the join.
     */
    protected final FromItem left;

    /**
     * Right item of the join.
     */
    protected final TableReference right;

    /**
     * Creates a join. Since SQL joins are always left-associative, you can only chain joins using their left item,
     * i.e. right item can be only a table references, but not another join.
     *
     * @param left  left item of the join.
     * @param right right item of the join.
     */
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
