package com.bimedia.squiggle.criteria;

import com.bimedia.squiggle.Criteria;
import com.bimedia.squiggle.Table;
import com.bimedia.squiggle.output.Output;

import java.util.Set;

/**
 * See OR and AND
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public abstract class BaseLogicGroup implements Criteria {

    private String operator;
    private Criteria left;
    private Criteria right;

    public BaseLogicGroup(String operator, Criteria left, Criteria right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public void write(Output out) {
        out.print("( ");
        left.write(out);
        out.print(' ')
                .print(operator)
                .print(' ');
        right.write(out);
        out.print(" )");
    }

    public void addReferencedTablesTo(Set<Table> tables) {
        left.addReferencedTablesTo(tables);
        right.addReferencedTablesTo(tables);
    }
}
