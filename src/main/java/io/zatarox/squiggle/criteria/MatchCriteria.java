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

import io.zatarox.squiggle.Column;
import io.zatarox.squiggle.Criteria;
import io.zatarox.squiggle.Matchable;
import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.literal.BooleanLiteral;
import io.zatarox.squiggle.literal.FloatLiteral;
import io.zatarox.squiggle.literal.IntegerLiteral;
import io.zatarox.squiggle.literal.StringLiteral;
import io.zatarox.squiggle.output.Output;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class MatchCriteria implements Criteria {

    public static final String EQUALS = "=";
    public static final String GREATER = ">";
    public static final String GREATEREQUAL = ">=";
    public static final String LESS = "<";
    public static final String LESSEQUAL = "<=";
    public static final String LIKE = "LIKE";
    public static final String NOTEQUAL = "<>";

    private final Matchable left;
    private final String operator;
    private final Matchable right;

    public MatchCriteria(Matchable left, String operator, Matchable right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public MatchCriteria(Column column, String matchType, boolean value) {
        this(column, matchType, new BooleanLiteral(value));
    }

    /**
     * Initializes a MatchCriteria with a given column, comparison operator, and
     * date operand that the criteria will use to make a comparison between the
     * given column and the date.
     *
     * @param column the column to use in the date comparison.
     * @param operator the comparison operator to use in the date comparison.
     * @param operand the date literal to use in the comparison.
     */
    public MatchCriteria(Column column, String operator, Date operand) {
        this(column, operator, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(operand));
    }

    public MatchCriteria(Column column, String matchType, double value) {
        this(column, matchType, new FloatLiteral(value));
    }

    public MatchCriteria(Column column, String matchType, long value) {
        this(column, matchType, new IntegerLiteral(value));
    }

    public MatchCriteria(Column column, String matchType, String value) {
        this(column, matchType, new StringLiteral(value));
    }

    public MatchCriteria(Table table, String columnname, String matchType, boolean value) {
        this(table.getColumn(columnname), matchType, value);
    }

    /**
     * Initializes a MatchCriteria with a table, column name is this table,
     * comparison operator, and date operand that the criteria will use to make
     * a comparison between the given table column and the date.
     *
     * @param table the table that contains a column having the given name to
     * use in the date comparison.
     * @param columnName the name of the column to use in the date comparison.
     * @param operator the comparison operator to use in the date comparison.
     * @param operand the date literal to use in the comparison.
     */
    public MatchCriteria(Table table, String columnName, String operator, Date operand) {
        this(table.getColumn(columnName), operator, operand);
    }

    public MatchCriteria(Table table, String columnname, String matchType, double value) {
        this(table.getColumn(columnname), matchType, value);
    }

    public MatchCriteria(Table table, String columnname, String matchType, long value) {
        this(table.getColumn(columnname), matchType, value);
    }

    public MatchCriteria(Table table, String columnname, String matchType, String value) {
        this(table.getColumn(columnname), matchType, value);
    }

    public Matchable getLeft() {
        return left;
    }

    public String getComparisonOperator() {
        return operator;
    }

    public Matchable getRight() {
        return right;
    }

    public void write(Output out) {
        left.write(out);
        out.print(' ').print(operator).print(' ');
        right.write(out);
    }

    public void addReferencedTablesTo(Set<Table> tables) {
        left.addReferencedTablesTo(tables);
        right.addReferencedTablesTo(tables);
    }
}
