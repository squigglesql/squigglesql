package com.github.squigglesql.squigglesql.join;

import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;

/**
 * Cross join.
 *
 * @since 4.1.0
 */
public class CrossJoin extends Join {

    /**
     * Creates a cross join. Since SQL joins are always left-associative, you can only chain joins using their left
     * item, i.e. right item can be only a table references, but not another join.
     *
     * @param left  left item to join.
     * @param right right item to join.
     */
    public CrossJoin(FromItem left, TableReference right) {
        super(left, right);
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.writeln(left).write("CROSS JOIN ").write(right);
    }
}
