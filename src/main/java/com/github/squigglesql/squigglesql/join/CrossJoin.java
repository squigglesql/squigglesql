package com.github.squigglesql.squigglesql.join;

import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;

public class CrossJoin extends Join {

    public CrossJoin(FromItem left, TableReference right) {
        super(left, right);
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(left).write(" CROSS JOIN ").write(right);
    }
}
