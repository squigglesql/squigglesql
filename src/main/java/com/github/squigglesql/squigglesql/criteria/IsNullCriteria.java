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
 * Criteria representing "value IS NULL" SQL expression.
 */
public class IsNullCriteria implements Criteria {

    private final Matchable value;

    /**
     * Creates a criteria.
     *
     * @param value value to match.
     */
    public IsNullCriteria(Matchable value) {
        this.value = value;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(value).write(" IS NULL");
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        value.collectTableReferences(tableReferences);
    }
}
