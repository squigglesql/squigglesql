/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;

import java.util.Set;

/**
 * Criteria representing a binary SQL operator, e.g. "left = right". The most popular operators are available as static
 * constants. We recommend you to import a static wildcard of this class to use them.
 */
public class MatchCriteria implements Criteria {

    /**
     * SQL "equals" operator.
     */
    public static final String EQUALS = "=";

    /**
     * SQL "greater" operator.
     */
    public static final String GREATER = ">";

    /**
     * SQL "greater or equals" operator.
     */
    public static final String GREATEREQUAL = ">=";

    /**
     * SQL "less" operator.
     */
    public static final String LESS = "<";

    /**
     * SQL "less or equals" operator.
     */
    public static final String LESSEQUAL = "<=";

    /**
     * SQL "like" operator.
     */
    public static final String LIKE = "LIKE";

    /**
     * SQL "not equal" operator.
     */
    public static final String NOTEQUAL = "<>";

    private final Matchable left;
    private final String operator;
    private final Matchable right;

    /**
     * Creates a criteria.
     * @param left Left operand.
     * @param operator Operator.
     * @param right Right operand.
     */
    public MatchCriteria(Matchable left, String operator, Matchable right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(left).write(' ').write(operator).write(' ').write(right);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        left.collectTableReferences(tableReferences);
        right.collectTableReferences(tableReferences);
    }
}
