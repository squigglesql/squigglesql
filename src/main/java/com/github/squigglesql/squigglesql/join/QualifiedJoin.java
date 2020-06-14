package com.github.squigglesql.squigglesql.join;

import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.criteria.Criteria;

public class QualifiedJoin extends Join {

    private final QualifiedJoinKind joinKind;
    private final Criteria criteria;

    public QualifiedJoin(FromItem left, QualifiedJoinKind joinKind, TableReference right, Criteria criteria) {
        super(left, right);
        this.joinKind = joinKind;
        this.criteria = criteria;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(left).write(' ').write(joinKind.name()).write(" JOIN ").write(right)
                .write(" ON ").write(criteria);
    }
}
