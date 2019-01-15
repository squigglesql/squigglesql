package io.zatarox.squiggle.literal;

import io.zatarox.squiggle.QueryCompiler;

public abstract class AbstractStringLiteral extends Literal {

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write('\'').write(getValue().toString().replace("'", "''")).write('\'');
    }

    protected abstract Object getValue();
}
