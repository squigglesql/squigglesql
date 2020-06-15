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

import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.util.CollectionWriter;

import java.util.Collection;
import java.util.Set;

/**
 * Group of multiple criterias. The listed criterias will be joined with some operator. If the list is empty,
 * the criteria gets compiled to constant false or true value (depends on group kind).
 */
class CriteriaGroup extends Criteria {

    private final Collection<Criteria> criterias;
    private final String operator;

    /**
     * Creates a criteria group.
     *
     * @param criterias criterias to join. Considered never empty.
     */
    CriteriaGroup(Collection<Criteria> criterias, String operator) {
        this.criterias = criterias;
        this.operator = operator;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        CollectionWriter.writeCollection(compiler, criterias, operator, true, true);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        for (Criteria criteria : criterias) {
            criteria.collectTableReferences(tableReferences);
        }
    }
}
