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
package io.zatarox.squiggle;

import io.zatarox.squiggle.util.CollectionWriter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class FunctionCall implements Selectable {

    private final String function;
    private final Collection<Matchable> arguments;

    public FunctionCall(String function, Collection<Matchable> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public FunctionCall(String function, Matchable... arguments) {
        this(function, Arrays.asList(arguments));
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(function);
        if (arguments.isEmpty()) {
            compiler.write("()");
            return;
        }
        CollectionWriter.writeCollection(compiler, arguments, ", ", true, false);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        for (Matchable argument : arguments) {
            argument.collectTableReferences(tableReferences);
        }
    }
}
