/*
 * Copyright 2019-2020 Egor Nepomnyaschih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
