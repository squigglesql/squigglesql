package com.github.squigglesql.squigglesql.statement;

public interface StatementBuilder<S> extends Parametrized {

    S buildStatement();
}
