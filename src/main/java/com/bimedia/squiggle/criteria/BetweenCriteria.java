package com.bimedia.squiggle.criteria;

import com.bimedia.squiggle.Criteria;
import com.bimedia.squiggle.Matchable;
import com.bimedia.squiggle.Table;
import com.bimedia.squiggle.literal.BigDecimalLiteral;
import com.bimedia.squiggle.literal.DateTimeLiteral;
import com.bimedia.squiggle.literal.FloatLiteral;
import com.bimedia.squiggle.literal.IntegerLiteral;
import com.bimedia.squiggle.output.Output;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

/**
 * Class BetweenCriteria is a Criteria extension that generates the SQL syntax
 * for a BETWEEN operator in an SQL Where clause.
 *
 * @author <a href="mailto:derek@derekmahar.ca">Derek Mahar</a>
 * @author Nat Pryce
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
