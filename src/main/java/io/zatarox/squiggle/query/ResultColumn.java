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
package io.zatarox.squiggle.query;

import io.zatarox.squiggle.Compilable;
import io.zatarox.squiggle.QueryCompiler;
import io.zatarox.squiggle.Selectable;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.TableReferred;
import io.zatarox.squiggle.alias.Aliasable;

import java.util.Collections;
import java.util.Set;

public class ResultColumn implements Aliasable, Compilable, TableReferred {

    private final Selectable selectable;
    private final int index;

    ResultColumn(Selectable selectable, int index) {
        this.selectable = selectable;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(selectable);
        String alias = compiler.getAlias(this);
        if (alias != null) {
            compiler.write(" as ").write(alias);
        }
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        selectable.collectTableReferences(tableReferences);
    }

    @Override
    public Iterable<String> getPreferredAliases() {
        return Collections.emptyList();
    }
}
