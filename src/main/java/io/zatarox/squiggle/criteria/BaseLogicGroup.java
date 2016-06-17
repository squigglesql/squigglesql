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
package io.zatarox.squiggle.criteria;

import io.zatarox.squiggle.Criteria;
import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.output.Output;

import java.util.Set;

/**
 * See OR and AND
 */
public abstract class BaseLogicGroup implements Criteria {

    private final String operator;
    private final Criteria left;
    private final Criteria right;

    public BaseLogicGroup(String operator, Criteria left, Criteria right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public void write(Output out) {
        out.print("( ");
        left.write(out);
        out.print(' ')
                .print(operator)
                .print(' ');
        right.write(out);
        out.print(" )");
    }

    @Override
    public void addReferencedTablesTo(Set<Table> tables) {
        left.addReferencedTablesTo(tables);
        right.addReferencedTablesTo(tables);
    }
}
