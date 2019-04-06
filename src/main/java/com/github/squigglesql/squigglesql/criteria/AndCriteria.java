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

import java.util.Collection;

/**
 * Conjunction of multiple criterias. The listed criterias will be joined with "AND" operator. If the list is empty,
 * the criteria gets compiled to "1 = 1" which is constantly true.
 */
public class AndCriteria extends CriteriaGroup {

    /**
     * Creates a conjunction criteria.
     * @param criterias criterias to join.
     */
    public AndCriteria(Collection<Criteria> criterias) {
        super(criterias);
    }

    /**
     * Creates a conjunction criteria.
     * @param criterias criterias to join.
     */
    public AndCriteria(Criteria... criterias) {
        super(criterias);
    }

    @Override
    protected String getOperator() {
        return " AND";
    }

    @Override
    protected boolean isEmptyTrue() {
        return true;
    }
}
