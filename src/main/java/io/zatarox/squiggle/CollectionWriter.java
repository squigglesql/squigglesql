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
package io.zatarox.squiggle;

import java.util.Collection;
import java.util.Iterator;

public abstract class CollectionWriter {

    public static void writeCollection(Output output, Collection<? extends Outputable> collection, String separator,
                                       boolean brackets, boolean multiLine) {
        if (collection.isEmpty()) {
            throw new RuntimeException("Can't write empty collection as SQL.");
        }
        Iterator<? extends Outputable> iterator = collection.iterator();
        Outputable outputable = iterator.next();
        if (!iterator.hasNext()) {
            writeOpenBracket(output, brackets);
            outputable.write(output);
            writeCloseBracket(output, brackets);
            return;
        }
        writeOpenBracket(output, brackets);
        indent(output, brackets, multiLine);
        while (true) {
            outputable.write(output);
            if (!iterator.hasNext()) {
                break;
            }
            separate(output, multiLine, separator);
            outputable = iterator.next();
        }
        unindent(output, brackets, multiLine);
        writeCloseBracket(output, brackets);
    }

    private static void writeOpenBracket(Output output, boolean brackets) {
        if (brackets) {
            output.write('(');
        } else {
            output.writeln().indent();
        }
    }

    private static void writeCloseBracket(Output output, boolean brackets) {
        if (brackets) {
            output.write(')');
        } else {
            output.writeln().unindent();
        }
    }

    private static void indent(Output output, boolean brackets, boolean multiLine) {
        if (brackets && multiLine) {
            output.writeln().indent();
        }
    }

    private static void separate(Output output, boolean multiLine, String separator) {
        if (multiLine) {
            output.writeln(separator);
        } else {
            output.write(separator);
        }
    }

    private static void unindent(Output output, boolean brackets, boolean multiLine) {
        if (brackets && multiLine) {
            output.writeln().unindent();
        }
    }
}
