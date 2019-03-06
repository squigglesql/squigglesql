package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.util.CollectionWriter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CollectionWriterTest {

    private QueryCompiler compiler;

    @Before
    public void setUp() {
        compiler = new QueryCompiler(new Output());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty() {
        CollectionWriter.writeCollection(compiler, Collections.emptyList(), ", ", false, false);
    }

    @Test
    public void testSingle() {
        CollectionWriter.writeCollection(compiler, generate(1), ", ", false, false);
        assertEquals("1", compiler.getOutput().toString());

        compiler.write('e'); // writeln doesn't insert a line break immediately - this call forces it
        assertEquals("1e", compiler.getOutput().toString());
    }

    @Test
    public void testSingleBrackets() {
        CollectionWriter.writeCollection(compiler, generate(1), ", ", true, false);
        assertEquals("(1)", compiler.getOutput().toString());

        compiler.write('e'); // writeln doesn't insert a line break immediately - this call forces it
        assertEquals("(1)e", compiler.getOutput().toString());
    }

    @Test
    public void testSingleMultiLine() {
        CollectionWriter.writeCollection(compiler, generate(1), ", ", false, true);
        assertEquals("\n"
                + "    1", compiler.getOutput().toString());

        compiler.write('e'); // writeln doesn't insert a line break immediately - this call forces it
        assertEquals("\n"
                + "    1\n"
                + "e", compiler.getOutput().toString());
    }

    @Test
    public void testSingleMultiLineBrackets() {
        CollectionWriter.writeCollection(compiler, generate(1), ", ", true, true);
        assertEquals("(1)", compiler.getOutput().toString());

        compiler.write('e'); // writeln doesn't insert a line break immediately - this call forces it
        assertEquals("(1)e", compiler.getOutput().toString());
    }

    @Test
    public void testMultiple() {
        CollectionWriter.writeCollection(compiler, generate(3), ", ", false, false);
        assertEquals("1, 2, 3", compiler.getOutput().toString());

        compiler.write('e'); // writeln doesn't insert a line break immediately - this call forces it
        assertEquals("1, 2, 3e", compiler.getOutput().toString());
    }

    @Test
    public void testMultipleBrackets() {
        CollectionWriter.writeCollection(compiler, generate(3), ", ", true, false);
        assertEquals("(1, 2, 3)", compiler.getOutput().toString());

        compiler.write('e'); // writeln doesn't insert a line break immediately - this call forces it
        assertEquals("(1, 2, 3)e", compiler.getOutput().toString());
    }

    @Test
    public void testMultipleMultiLine() {
        CollectionWriter.writeCollection(compiler, generate(3), ", ", false, true);
        assertEquals("\n"
                + "    1, \n"
                + "    2, \n"
                + "    3", compiler.getOutput().toString());

        compiler.write('e'); // writeln doesn't insert a line break immediately - this call forces it
        assertEquals("\n"
                + "    1, \n"
                + "    2, \n"
                + "    3\n"
                + "e", compiler.getOutput().toString());
    }

    @Test
    public void testMultipleMultiLineBrackets() {
        CollectionWriter.writeCollection(compiler, generate(3), ", ", true, true);
        assertEquals("(\n"
                + "    1, \n"
                + "    2, \n"
                + "    3\n"
                + ")", compiler.getOutput().toString());

        compiler.write('e'); // writeln doesn't insert a line break immediately - this call forces it
        assertEquals("(\n"
                + "    1, \n"
                + "    2, \n"
                + "    3\n"
                + ")e", compiler.getOutput().toString());
    }

    private Collection<MockCompilable> generate(int count) {
        List<MockCompilable> result = new ArrayList<>(count);
        for (int i = 1; i <= count; ++i) {
            result.add(new MockCompilable(i));
        }
        return result;
    }

    private static class MockCompilable implements Compilable {

        private final int value;

        public MockCompilable(int value) {
            this.value = value;
        }

        @Override
        public void compile(QueryCompiler compiler) {
            compiler.write(Integer.toString(value));
        }
    }
}
