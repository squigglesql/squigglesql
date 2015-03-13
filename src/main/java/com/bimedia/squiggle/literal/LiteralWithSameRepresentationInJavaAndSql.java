package com.bimedia.squiggle.literal;

import com.bimedia.squiggle.Literal;
import com.bimedia.squiggle.output.Output;

/**
 * @author Nat Pryce
 */
public abstract class LiteralWithSameRepresentationInJavaAndSql extends Literal {

    private final Object literalValue;

    protected LiteralWithSameRepresentationInJavaAndSql(Object literalValue) {
        this.literalValue = literalValue;
    }

    public void write(Output out) {
        out.print(literalValue);
    }
}
