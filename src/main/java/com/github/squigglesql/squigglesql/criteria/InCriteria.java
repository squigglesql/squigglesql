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
import com.github.squigglesql.squigglesql.util.CollectionWriter;

import java.util.Collection;
import java.util.Set;

/**
 * Criteria representing "value IN (...options)" expression. If the list of options is empty, gets compiled to "0 = 1"
 * which is constantly false.
 */
class InCriteria extends Criteria {

    private final Matchable value;
    private final Collection<Matchable> options;

    /**
     * Creates a criteria.
     *
     * @param value   value to match.
     * @param options options to match the value against. Considered never empty.
     */
    InCriteria(Matchable value, Collection<Matchable> options) {
        this.value = value;
        this.options = options;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(value).write(" IN ");
        CollectionWriter.writeCollection(compiler, options, ", ", true, false);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        value.collectTableReferences(tableReferences);
        for (Matchable option : options) {
            option.collectTableReferences(tableReferences);
        }
    }
}
