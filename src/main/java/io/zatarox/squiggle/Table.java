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
import io.zatarox.squiggle.output.Outputable;

public class Table implements Outputable {

    private final String name;
    private final String alias;

    public Table(String name) {
        this(name, null);
    }

    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    /**
     * Name of table
     */
    public String getName() {
        return name;
    }

    /**
     * Whether this table has an alias assigned.
     */
    private boolean hasAlias() {
        return alias != null;
    }

    /**
     * Short alias of table
     */
    public String getAlias() {
        return alias != null ? alias : name;
    }

    /**
     * Get a column for a particular table.
     */
    public Column getColumn(String columnName) {
        return new Column(this, columnName);
    }

    public WildCardColumn getWildcard() {
        return new WildCardColumn(this);
    }

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

        Table that = (Table) o;

        return getAlias().equals(that.getAlias());
    }

    public int hashCode() {
        return getAlias().hashCode();
    }

    public void write(Output out) {
        out.print(getName());
        if (hasAlias()) {
            out.print(' ');
            out.print(getAlias());
        }
    }
}
