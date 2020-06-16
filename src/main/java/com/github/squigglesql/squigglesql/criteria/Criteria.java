/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.Compilable;
import com.github.squigglesql.squigglesql.Matchable;
import com.github.squigglesql.squigglesql.TableReferred;

import java.util.Arrays;
import java.util.Collection;

/**
 * Criteria is a part of "WHERE" section of an SQL query. You may add multiple criterias to the query. They will be
 * joined with "AND" operator. All built-in implementations of Criteria are encapsulated in static methods of this
 * class. You may create your own criteria by extending this class.
 */
public abstract class Criteria implements Compilable, TableReferred {

    /**
     * Instantiates a criteria representing "value IS NULL" SQL expression.
     *
     * @param value value to match.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria isNull(Matchable value) {
        return new IsNullCriteria(value);
    }

    /**
     * Instantiates a criteria representing "value IS NOT NULL" SQL expression.
     *
     * @param value value to match.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria isNotNull(Matchable value) {
        return new IsNotNullCriteria(value);
    }

    /**
     * Instantiates a criteria representing SQL "=" operator. If you need to match nullable values, consider using
     * {@link #notDistinct(Matchable, Matchable)} criteria instead.
     *
     * @param left  left operand.
     * @param right right operand.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria equal(Matchable left, Matchable right) {
        return new MatchCriteria(left, " = ", right);
    }

    /**
     * Instantiates a criteria representing SQL "&lt;&gt;" operator. If you need to match nullable values, consider
     * using {@link #distinct(Matchable, Matchable)} criteria instead.
     *
     * @param left  left operand.
     * @param right right operand.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria notEqual(Matchable left, Matchable right) {
        return new MatchCriteria(left, " <> ", right);
    }

    /**
     * Instantiates a criteria representing SQL "&gt;" operator.
     *
     * @param left  left operand.
     * @param right right operand.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria greater(Matchable left, Matchable right) {
        return new MatchCriteria(left, " > ", right);
    }

    /**
     * Instantiates a criteria representing SQL "&lt;=" operator.
     *
     * @param left  left operand.
     * @param right right operand.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria notGreater(Matchable left, Matchable right) {
        return new MatchCriteria(left, " <= ", right);
    }

    /**
     * Instantiates a criteria representing SQL "&lt;" operator.
     *
     * @param left  left operand.
     * @param right right operand.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria less(Matchable left, Matchable right) {
        return new MatchCriteria(left, " < ", right);
    }

    /**
     * Instantiates a criteria representing SQL "&gt;=" operator.
     *
     * @param left  left operand.
     * @param right right operand.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria notLess(Matchable left, Matchable right) {
        return new MatchCriteria(left, " >= ", right);
    }

    /**
     * Instantiates a criteria representing SQL "LIKE" operator.
     *
     * @param left  left operand.
     * @param right right operand.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria like(Matchable left, Matchable right) {
        return new MatchCriteria(left, " LIKE ", right);
    }

    /**
     * Instantiates a criteria representing SQL "IS DISTINCT FROM" operator.
     *
     * @param left  left operand.
     * @param right right operand.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria distinct(Matchable left, Matchable right) {
        return new IsDistinctFromCriteria(left, right);
    }

    /**
     * Instantiates a criteria representing SQL "IS NOT DISTINCT FROM" operator.
     *
     * @param left  left operand.
     * @param right right operand.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria notDistinct(Matchable left, Matchable right) {
        return new IsNotDistinctFromCriteria(left, right);
    }

    /**
     * Instantiates a criteria representing a conjunction of multiple criteria. The listed criteria will be joined with
     * "AND" operator. If the list is empty, the criteria gets compiled to "1 = 1" which is constantly true.
     *
     * @param criteria criteria to join.
     * @return conjunction criteria.
     * @since 4.1.0
     */
    public static Criteria and(Collection<Criteria> criteria) {
        return criteria.isEmpty() ? LiteralCriteria.TRUE : new CriteriaGroup(criteria, " AND");
    }

    /**
     * Instantiates a criteria representing a conjunction of multiple criteria. The listed criteria will be joined with
     * "AND" operator. If the list is empty, the criteria gets compiled to "1 = 1" which is constantly true.
     *
     * @param criteria criteria to join.
     * @return conjunction criteria.
     * @since 4.1.0
     */
    public static Criteria and(Criteria... criteria) {
        return criteria.length == 0 ? LiteralCriteria.TRUE : new CriteriaGroup(Arrays.asList(criteria), " AND");
    }

    /**
     * Instantiates a criteria representing a disjunction of multiple criteria. The listed criteria will be joined with
     * "OR" operator. If the list is empty, the criteria gets compiled to "0 = 1" which is constantly false.
     *
     * @param criteria criteria to join.
     * @return conjunction criteria.
     * @since 4.1.0
     */
    public static Criteria or(Collection<Criteria> criteria) {
        return criteria.isEmpty() ? LiteralCriteria.FALSE : new CriteriaGroup(criteria, " OR");
    }

    /**
     * Instantiates a criteria representing a disjunction of multiple criteria. The listed criteria will be joined with
     * "OR" operator. If the list is empty, the criteria gets compiled to "0 = 1" which is constantly false.
     *
     * @param criteria criteria to join.
     * @return disjunction criteria.
     * @since 4.1.0
     */
    public static Criteria or(Criteria... criteria) {
        return criteria.length == 0 ? LiteralCriteria.FALSE : new CriteriaGroup(Arrays.asList(criteria), " OR");
    }

    /**
     * Instantiates a criteria representing "value BETWEEN lower AND upper" SQL expression.
     *
     * @param value value to match.
     * @param lower lower limit.
     * @param upper upper limit.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria between(Matchable value, Matchable lower, Matchable upper) {
        return new BetweenCriteria(value, lower, upper);
    }

    /**
     * Instantiates a criteria representing "value IN (...options)" expression. If the list of options is empty, gets
     * compiled to "0 = 1" which is constantly false.
     *
     * @param value   value to match.
     * @param options options to match the value against.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria in(Matchable value, Collection<Matchable> options) {
        return options.isEmpty() ? LiteralCriteria.FALSE : new InCriteria(value, options);
    }

    /**
     * Instantiates a criteria representing "value IN (...options)" expression. If the list of options is empty, gets
     * compiled to "0 = 1" which is constantly false.
     *
     * @param value   value to match.
     * @param options options to match the value against.
     * @return criteria.
     * @since 4.1.0
     */
    public static Criteria in(Matchable value, Matchable... options) {
        return options.length == 0 ? LiteralCriteria.FALSE : new InCriteria(value, Arrays.asList(options));
    }

    /**
     * Instantiates a criteria representing "NOT (criteria)" SQL expression.
     *
     * @param criteria criteria to negate.
     * @return negated criteria.
     * @since 4.1.0
     */
    public static Criteria not(Criteria criteria) {
        return new NotCriteria(criteria);
    }
}
