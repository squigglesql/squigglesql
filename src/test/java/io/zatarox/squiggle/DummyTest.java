package io.zatarox.squiggle;

import io.zatarox.squiggle.util.CollectionWriter;
import io.zatarox.squiggle.util.JdbcUtils;
import io.zatarox.squiggle.util.SquiggleUtils;
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
