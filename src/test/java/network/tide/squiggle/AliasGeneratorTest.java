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
package network.tide.squiggle;

import network.tide.squiggle.alias.AliasGenerator;
import network.tide.squiggle.alias.Aliasable;
import network.tide.squiggle.alias.Alphabet;
import network.tide.squiggle.alias.PreferredAliases;
import org.junit.Test;

import static network.tide.squiggle.alias.AliasGenerator.generateAlphabetic;
import static org.junit.Assert.assertEquals;

public class AliasGeneratorTest {

    private static final Alphabet ALPHABET = new Alphabet('a', 3);

    @Test
    public void testAlphabetic() {
        assertEquals("a", generateAlphabetic(0, ALPHABET));
        assertEquals("b", generateAlphabetic(1, ALPHABET));
        assertEquals("c", generateAlphabetic(2, ALPHABET));
        assertEquals("aa", generateAlphabetic(3, ALPHABET));
        assertEquals("ab", generateAlphabetic(4, ALPHABET));
        assertEquals("ac", generateAlphabetic(5, ALPHABET));
        assertEquals("ba", generateAlphabetic(6, ALPHABET));
        assertEquals("bb", generateAlphabetic(7, ALPHABET));
        assertEquals("bc", generateAlphabetic(8, ALPHABET));
        assertEquals("ca", generateAlphabetic(9, ALPHABET));
        assertEquals("cb", generateAlphabetic(10, ALPHABET));
        assertEquals("cc", generateAlphabetic(11, ALPHABET));
        assertEquals("aaa", generateAlphabetic(12, ALPHABET));
        assertEquals("aab", generateAlphabetic(13, ALPHABET));
        assertEquals("aac", generateAlphabetic(14, ALPHABET));
        assertEquals("aba", generateAlphabetic(15, ALPHABET));
    }

    @Test
    public void testAliasable() {
        AliasGenerator generator = new AliasGenerator(ALPHABET);
        assertEquals("h", generator.generateAlias(new MockAliasable("hello")));
        assertEquals("he", generator.generateAlias(new MockAliasable("hello")));
        assertEquals("hel", generator.generateAlias(new MockAliasable("h_ello")));
        assertEquals("a", generator.generateAlias(new MockAliasable("hello")));
        assertEquals("ab", generator.generateAlias(new MockAliasable("abstract")));
        assertEquals("b", generator.generateAlias(new MockAliasable("behemoth")));
        assertEquals("c", generator.generateAlias(new MockAliasable("hello")));
        assertEquals("be", generator.generateAlias(new MockAliasable("behemoth")));
        assertEquals("aa", generator.generateAlias(new MockAliasable("hello")));
    }

    private static class MockAliasable implements Aliasable {

        private final String name;

        MockAliasable(String name) {
            this.name = name;
        }

        @Override
        public Iterable<String> getPreferredAliases() {
            return new PreferredAliases(name);
        }
    }
}
