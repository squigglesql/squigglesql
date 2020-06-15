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
 * Criteria is a part of "WHERE" section of an SQL query. You may add many criterias to the query. They will be joined
 * with "AND" operator.
 */
public abstract class Criteria implements Compilable, TableReferred {

    public static Criteria isNull(Matchable value) {
        return new IsNullCriteria(value);
    }

    public static Criteria isNotNull(Matchable value) {
        return new IsNotNullCriteria(value);
    }

    public static Criteria equal(Matchable left, Matchable right) {
        return new MatchCriteria(left, " = ", right);
    }

    public static Criteria notEqual(Matchable left, Matchable right) {
        return new MatchCriteria(left, " <> ", right);
    }

    public static Criteria greater(Matchable left, Matchable right) {
        return new MatchCriteria(left, " > ", right);
    }

    public static Criteria notGreater(Matchable left, Matchable right) {
        return new MatchCriteria(left, " <= ", right);
    }

    public static Criteria less(Matchable left, Matchable right) {
        return new MatchCriteria(left, " < ", right);
    }

    public static Criteria notLess(Matchable left, Matchable right) {
        return new MatchCriteria(left, " >= ", right);
    }

    public static Criteria like(Matchable left, Matchable right) {
        return new MatchCriteria(left, " LIKE ", right);
    }

    public static Criteria distinct(Matchable left, Matchable right) {
        return new IsDistinctFromCriteria(left, right);
    }

    public static Criteria notDistinct(Matchable left, Matchable right) {
        return new IsNotDistinctFromCriteria(left, right);
    }

    public static Criteria and(Collection<Criteria> criterias) {
        return criterias.isEmpty() ? LiteralCriteria.TRUE : new CriteriaGroup(criterias, " AND");
    }

    public static Criteria and(Criteria... criterias) {
        return criterias.length == 0 ? LiteralCriteria.TRUE : new CriteriaGroup(Arrays.asList(criterias), " AND");
    }

    public static Criteria or(Collection<Criteria> criterias) {
        return criterias.isEmpty() ? LiteralCriteria.FALSE : new CriteriaGroup(criterias, " OR");
    }

    public static Criteria or(Criteria... criterias) {
        return criterias.length == 0 ? LiteralCriteria.FALSE : new CriteriaGroup(Arrays.asList(criterias), " OR");
    }

    public static Criteria between(Matchable value, Matchable lower, Matchable upper) {
        return new BetweenCriteria(value, lower, upper);
    }

    public static Criteria in(Matchable value, Collection<Matchable> options) {
        return options.isEmpty() ? LiteralCriteria.FALSE : new InCriteria(value, options);
    }

    public static Criteria in(Matchable value, Matchable... options) {
        return options.length == 0 ? LiteralCriteria.FALSE : new InCriteria(value, Arrays.asList(options));
    }

    public static Criteria not(Criteria criteria) {
        return new NotCriteria(criteria);
    }
}
