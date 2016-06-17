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
package io.zatarox.squiggle.criteria;

import io.zatarox.squiggle.Criteria;
import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.output.Output;

import java.util.List;
import java.util.Set;

/**
 * Class CriteriaExpression is Criteria class extension that generates the SQL
 * syntax for a compound logic expression in an SQL Where clause. The logic
 * expression that the class generates is a list of criteria terms each
 * separated by an {@link Operator#AND AND} or {@link Operator#OR OR} operator.
 * Formally, we may express a logic expression in Backus-Naur Form (BNF) as
 * <p>
 * <code>
 * &lt;expression&gt; ::= &lt;term&gt; | &lt;term&gt; &lt;operator &lt;expression&gt;
 * <br/>&lt;term&gt; ::= &lt;criteria&gt;
 * <br/>&lt;operator&gt; ::= AND | OR
 * </code>
 * </p>
 */
public class CriteriaExpression implements Criteria {

    /**
     * Operator identifiers
     */
    public enum Operator {

        AND, OR;
    }

    // Recursive reference to another expression.
    private CriteriaExpression expression;

    // Operator that joins the initial criteria term with the trailing criteria
    // expression.
    private Operator operator;

    // Initial criteria term in the expression.
    private Criteria term;

    /**
     * Initializes a CriteriaExpression with a single criteria term, leaving the
     * trailing criteria expression set to null. This constructor definition
     * represents the rule
     *
     * &lt;expression&gt; ::= &lt;term&gt;
     *
     * in the formal criteria expression syntax.
     *
     * @param term single criteria to assign to this criteria expression.
     */
    public CriteriaExpression(final Criteria term) {
        this.term = term;
    }

    /**
     * Initializes a CriteriaExpression with an initial criteria term, an
     * operator, and a another trailing criteria expression. Note that this
     * constructor definition is recursive and represents the rule
     *
     * &lt;expression&gt; ::= &lt;term&gt; &lt;operator &lt;expression&gt;
     *
     * in the formal criteria expression syntax.
     *
     * @param term the starting criteria to assign to the new criteria
     * expression.
     *
     * @param operator the infix operator, either {@link Operator#AND AND} or
     * {@link Operator#OR OR}, that joins the initial criteria with the trailing
     * criteria expression.
     *
     * @param expression the trailing expression to assign to the new criteria
     * expression.
     */
    public CriteriaExpression(final Criteria term, Operator operator,
            CriteriaExpression expression) {
        this(term);
        this.operator = operator;
        this.expression = expression;
    }

    /**
     * Recursively generates a CriteriaExpression from a given list of criteria
     * and an infix operator to join each criteria with the next in the list.
     *
     * @param terms the list of criteria terms from which the constructor
     * generates the new criteria expression.
     *
     * @param operator the infix operator, either {@link Operator#AND AND} or
     * {@link Operator#OR OR} that joins each criteria term with the next in the
     * list.
     */
    public CriteriaExpression(final List<Criteria> terms, Operator operator) {
        this.operator = operator;
        if (terms.size() == 0)
			; else {
            this.term = terms.get(0);
            if (terms.size() > 1) {
                this.expression = new CriteriaExpression(terms.subList(1, terms
                        .size()), operator);
            }
        }
    }

    /**
     * Returns the trailing expression in this criteria expression.
     *
     * @return the trailing expression in this criteria expression.
     */
    public CriteriaExpression getExpression() {
        return expression;
    }

    /**
     * Returns the operator, either {@link Operator#AND AND} or
     * {@link Operator#OR OR}, in this criteria expression that joins the
     * initial criteria with the trailing criteria expression.
     *
     * @return the operator, either {@link Operator#AND AND} or
     * {@link Operator#OR OR}, in this criteria expression.
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the initial criteria in this criteria expression.
     *
     * @return the initial criteria in this criteria expression.
     */
    public Criteria getTerm() {
        return term;
    }

    /**
     * Writes this criteria expression to the given output destination. If both
     * term and expression are null, this method generates no output. If
     * expression is null, but term is defined, the method writes the term
     * criteria to the output. If both expression and term are defined, however,
     * the method creates a new logic operator object, either
     * {@link Operator#AND AND} or {@link Operator#OR OR}, assigns it the term
     * and expression, and writes this new operator to the output.
     *
     * @param out the output destination to which to write this criteria
     * expression.
     *
     * @see
     * io.zatarox.squiggle.Criteria#write(io.zatarox.squiggle.output.Output)
     */
    public void write(Output out) {
        if (term == null && expression == null) {
        } else if (expression == null) {
            term.write(out);
        } else if (operator == Operator.AND) {
            new AND(term, expression).write(out);
        } else if (operator == Operator.OR) {
            new OR(term, expression).write(out);
        }
    }

    public void addReferencedTablesTo(Set<Table> tables) {
        term.addReferencedTablesTo(tables);
        if (expression != null) {
            expression.addReferencedTablesTo(tables);
        }
    }
}
