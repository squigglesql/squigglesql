package com.github.squigglesql.squigglesql.join;

import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.criteria.Criteria;

/**
 * Qualified is a join kind which can be supplied with an ON expression. CROSS JOIN is not a qualified join kind,
 * because it doesn't have an ON expression, so it is represented by a separate Join subclass.
 * <p>
 * MySQL doesn't support FULL JOIN.
 *
 * @since 4.1.0
 */
public class QualifiedJoin extends Join {

    private final QualifiedJoinKind joinKind;
    private final Criteria criteria;

    /**
     * Creates a qualified join. Since SQL joins are always left-associative, you can only chain joins using their left
     * item, i.e. right item can be only a table references, but not another join.
     *
     * @param left     left item of the join.
     * @param joinKind qualified join kind.
     * @param right    right item of the join.
     * @param criteria join criteria. Usually matches two columns in the left and the right parts of the join.
     */
    public QualifiedJoin(FromItem left, QualifiedJoinKind joinKind, TableReference right, Criteria criteria) {
        super(left, right);
        this.joinKind = joinKind;
        this.criteria = criteria;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.writeln(left).write(joinKind.name()).write(" JOIN ").write(right)
                .write(" ON ").write(criteria);
    }
}
