/*
 * Copyright 2019 Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql;

import java.util.Set;

/**
 * SQL type cast expression.
 */
public class TypeCast implements Selectable {

    private final Matchable value;
    private final String type;

    /**
     * Creates an expression.
     *
     * @param value value to cast.
     * @param type  SQL type to cast the value to.
     */
    public TypeCast(Matchable value, String type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(value).write("::").write(type); // quote call doesn't work for built-in types
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        value.collectTableReferences(tableReferences);
    }
}
