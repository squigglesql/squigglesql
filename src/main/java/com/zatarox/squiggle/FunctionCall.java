/*
 * Copyright 2004-2015 Joe Walnes, Guillaume Chauvet.
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
package com.zatarox.squiggle;

import java.util.Set;

import com.zatarox.squiggle.output.Output;

public class FunctionCall implements Matchable, Selectable {

    private final String functionName;
    private final Matchable[] arguments;

    public FunctionCall(String functionName, Matchable... arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public void write(Output out) {
        out.print(functionName).print("(");
        for (int i = 0; i < arguments.length; i++) {
            if (i > 0) {
                out.print(", ");
            }
            arguments[i].write(out);
        }
        out.print(")");
    }

    public void addReferencedTablesTo(Set<Table> tables) {
        for (Matchable argument : arguments) {
            argument.addReferencedTablesTo(tables);
        }
    }
}
