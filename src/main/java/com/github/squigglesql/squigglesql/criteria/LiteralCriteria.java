/*
 * Copyright 2020 Egor Nepomnyaschih.
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

import java.util.Set;

class LiteralCriteria extends Criteria {

    static final Criteria TRUE = new LiteralCriteria("1 = 1");
    static final Criteria FALSE = new LiteralCriteria("0 = 1");

    private final String literal;

    private LiteralCriteria(String literal) {
        this.literal = literal;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(literal);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
    }
}
