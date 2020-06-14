package com.github.squigglesql.squigglesql.join;

/**
 * All qualified join kinds. Qualified is a join kind which can be supplied with an ON expression. CROSS JOIN is not
 * a qualified join kind, because it doesn't have an ON expression, so it is represented by a separate Join subclass.
 * <p>
 * MySQL doesn't support FULL JOIN.
 *
 * @since 4.1.0
 */
public enum QualifiedJoinKind {

    INNER,
    LEFT,
    RIGHT,
    FULL
}
