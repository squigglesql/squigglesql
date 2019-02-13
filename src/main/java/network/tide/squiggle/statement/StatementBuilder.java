package network.tide.squiggle.statement;

public interface StatementBuilder<S> extends Parametrized {

    S buildStatement();
}
