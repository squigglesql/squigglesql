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

import com.zatarox.squiggle.output.Output;

public class ModifiedColumn extends Column {

    private final ColumnOperator operator;

    public enum ColumnOperator {

        COUNT("count"),
        DISTINCT("distinct");

        private final String sqlName;

        ColumnOperator(String sqlName) {
            this.sqlName = sqlName;
        }

        public String getSqlName() {
            return sqlName;
        }
    }

    public ModifiedColumn(Table table, String name, ColumnOperator operator) {
        super(table, name);
        this.operator = operator;
    }

    public ColumnOperator getOperator() {
        return operator;
    }

    @Override
    public void write(Output out) {
        out.print(getOperator().getSqlName() + "(")
                .print(getTable().getAlias())
                .print('.')
                .print(getName())
                .print(")");
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && ((ModifiedColumn) o).getOperator().equals(getOperator());
    }
}
