/*
 * Copyright 2004-2020 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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

class BetweenCriteria extends Criteria {

    private final Matchable value, lower, upper;

    BetweenCriteria(Matchable value, Matchable lower, Matchable upper) {
        this.value = value;
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(value).write(" BETWEEN ").write(lower).write(" AND ").write(upper);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        value.collectTableReferences(tableReferences);
        lower.collectTableReferences(tableReferences);
        upper.collectTableReferences(tableReferences);
    }
}
