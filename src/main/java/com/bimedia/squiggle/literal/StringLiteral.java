package com.bimedia.squiggle.literal;

import com.bimedia.squiggle.Literal;
import com.bimedia.squiggle.output.Output;

public class StringLiteral extends Literal {

    private final String literalValue;

    public StringLiteral(String literalValue) {
        this.literalValue = literalValue;
    }

    public void write(Output out) {
        out.print(quote(literalValue));
    }

    protected String quote(String s) {
        if (s == null) {
            return "null";
        }

        StringBuilder str = new StringBuilder();
        str.append('\'');
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\\'
                    || s.charAt(i) == '\"'
                    || s.charAt(i) == '\'') {
                str.append('\\');
            }
            str.append(s.charAt(i));
        }
        str.append('\'');
        return str.toString();
    }
}
