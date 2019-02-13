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
package network.tide.squiggle.query;

import network.tide.squiggle.BaseOrder;
import network.tide.squiggle.QueryCompiler;
import network.tide.squiggle.Selectable;

import java.util.Set;

/**
 * TODO: Find a way to implement deep ordering by ResultColumn, such as:
 * SELECT e.first_name as a, e.last_name as b FROM employee e ORDER BY concat(a, b)
 */
public class OrderBySelectable implements BaseOrder {

    private final Selectable selectable;
    private final boolean ascending;

    public OrderBySelectable(Selectable selectable, boolean ascending) {
        this.selectable = selectable;
        this.ascending = ascending;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(selectable);
        if (!ascending) {
            compiler.write(" DESC");
        }
    }

    @Override
    public void collectResultReferences(Set<ResultColumn> resultReferences) {
    }
}
