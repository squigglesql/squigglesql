package network.tide.squiggle;

import network.tide.squiggle.util.CollectionWriter;
import network.tide.squiggle.util.JdbcUtils;
import network.tide.squiggle.util.SquiggleUtils;
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
