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
package com.github.squigglesql.squigglesql.alias;

/**
 * Preferred alias generator. Generates sequences of up to 3 letters the object name starts with. For example, if
 * object name is "order", it will generate the following aliases: "o", "or" and "ord".
 */
public class PreferredAliases implements Iterable<String> {

    private static final int LIMIT = 3;

    private final String base;

    /**
     * Creates a preferred alias generator.
     * @param base Object name.
     */
    public PreferredAliases(String base) {
        this.base = base.replace("_", "");
    }

    @Override
    public java.util.Iterator<String> iterator() {
        return new Iterator(base);
    }

    private static class Iterator implements java.util.Iterator<String> {

        private final String base;
        private int nextSymbol = 0;

        Iterator(String base) {
            this.base = base;
        }

        @Override
        public boolean hasNext() {
            return nextSymbol < base.length() && nextSymbol < LIMIT;
        }

        @Override
        public String next() {
            return base.substring(0, ++nextSymbol);
        }
    }
}
