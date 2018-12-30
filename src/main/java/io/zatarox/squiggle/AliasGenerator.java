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

public abstract class AliasGenerator {

    public static final Alphabet DEFAULT_ALPHABET = new Alphabet('a', 26);

    public static String generateAlias(int index) {
        return generateAlias(index, DEFAULT_ALPHABET);
    }

    public static String generateAlias(int index, Alphabet alphabet) {
        StringBuilder result = new StringBuilder();
        while (index >= 0) {
            result.insert(0, (char)(alphabet.getFirstLetter() + index % alphabet.getLetterCount()));
            index = index / alphabet.getLetterCount() - 1;
        }
        return result.toString();
    }

    public static class Alphabet {

        private final char firstLetter;
        private final int letterCount;

        public Alphabet(char firstLetter, int letterCount) {
            this.firstLetter = firstLetter;
            this.letterCount = letterCount;
        }

        public char getFirstLetter() {
            return firstLetter;
        }

        public int getLetterCount() {
            return letterCount;
        }
    }
}