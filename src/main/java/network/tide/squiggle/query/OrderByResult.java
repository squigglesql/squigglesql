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
package network.tide.squiggle.query;

import network.tide.squiggle.BaseOrder;
import network.tide.squiggle.QueryCompiler;

import java.util.Set;

public class OrderByResult implements BaseOrder {

    private final ResultColumn column;
    private final boolean ascending;

    OrderByResult(ResultColumn column, boolean ascending) {
        this.column = column;
        this.ascending = ascending;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.quote(compiler.getAlias(column));
        if (!ascending) {
            compiler.write(" DESC");
        }
    }

    @Override
    public void collectResultReferences(Set<ResultColumn> resultReferences) {
        resultReferences.add(column);
    }
}
