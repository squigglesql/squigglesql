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
 * Alphabet - a range of latin letters.
 */
public class Alphabet {

    private final char firstLetter;
    private final int letterCount;

    /**
     * Creates an alphabet.
     *
     * @param firstLetter first letter of the alphabet.
     * @param letterCount count of letters in the alphabet.
     */
    public Alphabet(char firstLetter, int letterCount) {
        this.firstLetter = firstLetter;
        this.letterCount = letterCount;
    }

    /**
     * @return first letter of the alphabet.
     */
    public char getFirstLetter() {
        return firstLetter;
    }

    /**
     * @return count of letters in the alphabet.
     */
    public int getLetterCount() {
        return letterCount;
    }
}
