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
package io.zatarox.squiggle;

import io.zatarox.squiggle.output.Output;

public class Column extends Projection implements Matchable {

    private final String name;

    public Column(Table table, String name) {
        super(table);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void write(Output out) {
        out.print(getTable().getAlias()).print('.').print(getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = getTable().hashCode();
        result = prime * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }

        final Column that = (Column) o;

        return this.name.equals(that.name)
                && this.getTable().equals(that.getTable());
    }
}
