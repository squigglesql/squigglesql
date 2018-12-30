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
package io.zatarox.squiggle.criteria;

import io.zatarox.squiggle.Matchable;
import io.zatarox.squiggle.QueryCompiler;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.util.CollectionWriter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class InCriteria implements Criteria {

    private final Matchable value;
    private final Collection<Matchable> options;

    public InCriteria(Matchable value, Collection<Matchable> options) {
        this.value = value;
        this.options = options;
    }

    public InCriteria(Matchable value, Matchable... options) {
        this(value, Arrays.asList(options));
    }

    @Override
    public void compile(QueryCompiler compiler) {
        if (options.isEmpty()) {
            compiler.write("1 = 1");
            return;
        }
        compiler.write(value).write(" IN ");
        CollectionWriter.writeCollection(compiler, options, ", ", true, false);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        value.collectTableReferences(tableReferences);
        for (Matchable option : options) {
            option.collectTableReferences(tableReferences);
        }
    }
}
