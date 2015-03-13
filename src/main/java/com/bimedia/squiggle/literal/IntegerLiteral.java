package com.bimedia.squiggle.literal;

/**
 * @author Nat Pryce
 */
public class IntegerLiteral extends LiteralWithSameRepresentationInJavaAndSql {

    public IntegerLiteral(long literalValue) {
        super(new Long(literalValue));
    }
}
