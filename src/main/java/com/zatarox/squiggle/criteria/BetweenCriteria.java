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
package com.zatarox.squiggle.criteria;

import com.zatarox.squiggle.Criteria;
import com.zatarox.squiggle.Matchable;
import com.zatarox.squiggle.Table;
import com.zatarox.squiggle.literal.BigDecimalLiteral;
import com.zatarox.squiggle.literal.DateTimeLiteral;
import com.zatarox.squiggle.literal.FloatLiteral;
import com.zatarox.squiggle.literal.IntegerLiteral;
import com.zatarox.squiggle.output.Output;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

/**
 * Class BetweenCriteria is a Criteria extension that generates the SQL syntax
 * for a BETWEEN operator in an SQL Where clause.
 */
public class BetweenCriteria implements Criteria {

    private final Matchable column;
    private final Matchable lower, upper;

    /**
     * Initializes a new BetweenCriteria with an operand and the upper and lower
     * bounds of the SQL BETWEEN operator.
     *
     * @param operand the first operand to the SQL BETWEEN operator that the
     * operator uses to test whether the column falls within the given range.
     * The SQL type of the column must be DECIMAL or NUMERIC.
     * @param lower the lower bound of the BETWEEN operator
     * @param upper the upper bound of the BETWEEN operator
     */
    public BetweenCriteria(Matchable operand, Matchable lower, Matchable upper) {
        this.column = operand;
        this.lower = lower;
        this.upper = upper;
    }

    public BetweenCriteria(Matchable operand, BigDecimal lower, BigDecimal upper) {
        this(operand, new BigDecimalLiteral(lower), new BigDecimalLiteral(upper));
    }

    public BetweenCriteria(Matchable column, Date upper, Date lower) {
        this(column, new DateTimeLiteral(upper), new DateTimeLiteral(lower));
    }

    public BetweenCriteria(Matchable column, double lower, double upper) {
        this(column, new FloatLiteral(lower), new FloatLiteral(upper));
    }

    public BetweenCriteria(Matchable column, long lower, long upper) {
        this(column, new IntegerLiteral(lower), new IntegerLiteral(upper));
    }

    public void write(Output out) {
        column.write(out);
        out.print(" BETWEEN ");
        lower.write(out);
        out.print(" AND ");
        upper.write(out);
    }

    public void addReferencedTablesTo(Set<Table> tables) {
        column.addReferencedTablesTo(tables);
    }
}
