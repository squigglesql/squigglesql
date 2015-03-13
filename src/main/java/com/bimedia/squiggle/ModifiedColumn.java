package com.bimedia.squiggle;

import com.bimedia.squiggle.output.Output;

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
