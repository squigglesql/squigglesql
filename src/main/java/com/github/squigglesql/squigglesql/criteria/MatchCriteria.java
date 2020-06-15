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

/**
 * Criteria representing a binary SQL operator, e.g. "left = right". The most popular operators are available as static
 * constants. We recommend you to import a static wildcard of this class to use them.
 */
class MatchCriteria extends BinaryCriteria {

    private final String operator;

    /**
     * Creates a criteria.
     *
     * @param left     left operand.
     * @param operator operator.
     * @param right    right operand.
     */
    MatchCriteria(Matchable left, String operator, Matchable right) {
        super(left, right);
        this.operator = operator;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(left).write(operator).write(right);
    }
}
