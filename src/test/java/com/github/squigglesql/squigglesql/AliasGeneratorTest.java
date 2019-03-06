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
package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.alias.AliasGenerator;
import com.github.squigglesql.squigglesql.alias.Aliasable;
import com.github.squigglesql.squigglesql.alias.Alphabet;
import com.github.squigglesql.squigglesql.alias.PreferredAliases;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AliasGeneratorTest {

    private static final Alphabet ALPHABET = new Alphabet('a', 3);

    @Test
    public void testAlphabetic() {
        Assert.assertEquals("a", AliasGenerator.generateAlphabetic(0, ALPHABET));
        Assert.assertEquals("b", AliasGenerator.generateAlphabetic(1, ALPHABET));
        Assert.assertEquals("c", AliasGenerator.generateAlphabetic(2, ALPHABET));
        Assert.assertEquals("aa", AliasGenerator.generateAlphabetic(3, ALPHABET));
        Assert.assertEquals("ab", AliasGenerator.generateAlphabetic(4, ALPHABET));
        Assert.assertEquals("ac", AliasGenerator.generateAlphabetic(5, ALPHABET));
        Assert.assertEquals("ba", AliasGenerator.generateAlphabetic(6, ALPHABET));
        Assert.assertEquals("bb", AliasGenerator.generateAlphabetic(7, ALPHABET));
        Assert.assertEquals("bc", AliasGenerator.generateAlphabetic(8, ALPHABET));
        Assert.assertEquals("ca", AliasGenerator.generateAlphabetic(9, ALPHABET));
        Assert.assertEquals("cb", AliasGenerator.generateAlphabetic(10, ALPHABET));
        Assert.assertEquals("cc", AliasGenerator.generateAlphabetic(11, ALPHABET));
        Assert.assertEquals("aaa", AliasGenerator.generateAlphabetic(12, ALPHABET));
        Assert.assertEquals("aab", AliasGenerator.generateAlphabetic(13, ALPHABET));
        Assert.assertEquals("aac", AliasGenerator.generateAlphabetic(14, ALPHABET));
        Assert.assertEquals("aba", AliasGenerator.generateAlphabetic(15, ALPHABET));
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
