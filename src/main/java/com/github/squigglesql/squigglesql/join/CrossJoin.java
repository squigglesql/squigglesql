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
package com.github.squigglesql.squigglesql.join;

import com.github.squigglesql.squigglesql.FromItem;
import com.github.squigglesql.squigglesql.QueryCompiler;
import com.github.squigglesql.squigglesql.TableReference;

/**
 * Cross join.
 *
 * @since 4.1.0
 */
public class CrossJoin extends Join {

    /**
     * Creates a cross join. Since SQL joins are always left-associative, you can only chain joins using their left
     * item, i.e. right item can be only a table references, but not another join.
     *
     * @param left  left item to join.
     * @param right right item to join.
     */
    public CrossJoin(FromItem left, TableReference right) {
        super(left, right);
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.writeln(left).write("CROSS JOIN ").write(right);
    }
}
