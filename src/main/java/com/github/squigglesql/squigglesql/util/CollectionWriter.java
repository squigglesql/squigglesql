/*
 * Copyright 2019 Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.util;

import com.github.squigglesql.squigglesql.Compilable;
import com.github.squigglesql.squigglesql.QueryCompiler;

import java.util.Collection;
import java.util.Iterator;

/**
 * Utilities for collection writing to SQL query output.
 */
public abstract class CollectionWriter {

    /**
     * Writes a collection to SQL query output.
     *
     * @param compiler   compiler to write the output to.
     * @param collection collection.
     * @param separator  separator to use. Note: separator is appended directly after a value - it doesn't add a space
     *                   between them.
     * @param brackets   wrap the collection with brackets.
     * @param multiLine  write every value at a separate line unless the collection contains only one value.
     * @throws IllegalArgumentException if the collection is empty.
     */
    public static void writeCollection(QueryCompiler compiler, Collection<? extends Compilable> collection,
                                       String separator, boolean brackets, boolean multiLine) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Can't write empty collection as SQL.");
        }
        Iterator<? extends Compilable> iterator = collection.iterator();
        Compilable compilable = iterator.next();
        if (!iterator.hasNext()) {
            writeOpenBracket(compiler, brackets, multiLine);
            compilable.compile(compiler);
            writeCloseBracket(compiler, brackets, multiLine);
            return;
        }
        writeOpenBracket(compiler, brackets, multiLine);
        indent(compiler, brackets, multiLine);
        while (true) {
            compilable.compile(compiler);
            if (!iterator.hasNext()) {
                break;
            }
            separate(compiler, multiLine, separator);
            compilable = iterator.next();
        }
        unindent(compiler, brackets, multiLine);
        writeCloseBracket(compiler, brackets, multiLine);
    }

    private static void writeOpenBracket(QueryCompiler compiler, boolean brackets, boolean multiLine) {
        if (brackets) {
            compiler.write('(');
        } else if (multiLine) {
            compiler.writeln().indent();
        }
    }

    private static void writeCloseBracket(QueryCompiler compiler, boolean brackets, boolean multiLine) {
        if (brackets) {
            compiler.write(')');
        } else if (multiLine) {
            compiler.writeln().unindent();
        }
    }

    private static void indent(QueryCompiler compiler, boolean brackets, boolean multiLine) {
        if (brackets && multiLine) {
            compiler.writeln().indent();
        }
    }

    private static void separate(QueryCompiler compiler, boolean multiLine, String separator) {
        if (multiLine) {
            compiler.writeln(separator);
        } else {
            compiler.write(separator);
        }
    }

    private static void unindent(QueryCompiler compiler, boolean brackets, boolean multiLine) {
        if (brackets && multiLine) {
            compiler.writeln().unindent();
        }
    }
}
