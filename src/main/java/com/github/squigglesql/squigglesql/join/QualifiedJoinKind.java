package com.github.squigglesql.squigglesql.join;

/**
 * All qualified join kinds. Qualified is a join kind which can be supplied with an ON expression. CROSS JOIN is not in
 * a qualified join kind, because it doesn't have an ON expression, so it is represented by a separate Join subclass.
 * MySQL doesn't support FULL JOIN.
 */
public enum QualifiedJoinKind {

    INNER,
    LEFT,
    RIGHT,
    FULL
}
