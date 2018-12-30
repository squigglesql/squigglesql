package io.zatarox.squiggle.statement;

public interface StatementBuilder<S> extends Parametrized {

    S buildStatement();
}
