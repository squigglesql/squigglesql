/*
 * Copyright 2019-2020 Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.join;

import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.TableReference;

import java.util.Set;

/**
 * Abstract join.
 *
 * @since 4.1.0
 */
public abstract class Join implements FromItem {

    /**
     * Left item of the join.
     */
    protected final FromItem left;

    /**
     * Right item of the join.
     */
    protected final TableReference right;

    /**
     * Creates a join. Since SQL joins are always left-associative, you can only chain joins using their left item,
     * i.e. right item can be only a table references, but not another join.
     *
     * @param left  left item of the join.
     * @param right right item of the join.
     */
    protected Join(FromItem left, TableReference right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public abstract void compile(QueryCompiler compiler);

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        left.collectTableReferences(tableReferences);
        tableReferences.add(right);
    }
}
