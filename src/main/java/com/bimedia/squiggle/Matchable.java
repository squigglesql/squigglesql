package com.bimedia.squiggle;

import java.util.Set;

import com.bimedia.squiggle.output.Outputable;

/**
 * Something that can be part of a match expression in a where clause
 *
 * @author Nat Pryce
 */
public interface Matchable extends Outputable {

    void addReferencedTablesTo(Set<Table> tables);
}
