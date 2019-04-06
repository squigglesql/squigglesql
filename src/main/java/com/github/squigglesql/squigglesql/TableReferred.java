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
 * Part of an SQL query that contains a reference to a table. During query compilation, Squiggle collects all table
 * references and lists them in "FROM" section of the query.
 */
public interface TableReferred {

    /**
     * Extends a set with all necessary table references.
     *
     * @param tableReferences Set of the references to extend.
     */
    void collectTableReferences(Set<TableReference> tableReferences);
}
