package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.util.CollectionWriter;
import com.github.squigglesql.squigglesql.util.JdbcUtils;
import com.github.squigglesql.squigglesql.util.SquiggleUtils;
import org.junit.Test;

// Test to force coveralls.io ignore classes of utilities in coverage.
public class DummyTest {

    @Test
    public void testDummy() {
        new SquiggleUtils() {};
        new SquiggleConstants() {};
        new JdbcUtils() {};
        new CollectionWriter() {};
    }
}
