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

import io.zatarox.squiggle.util.AliasGenerator;
import org.junit.Test;

import static io.zatarox.squiggle.util.AliasGenerator.generateAlias;
import static org.junit.Assert.assertEquals;

public class AliasGeneratorTest {

    private static final AliasGenerator.Alphabet ALPHABET = new AliasGenerator.Alphabet('a', 3);

    @Test
    public void uniqueAlias() {
        assertEquals("a", generateAlias(0, ALPHABET));
        assertEquals("b", generateAlias(1, ALPHABET));
        assertEquals("c", generateAlias(2, ALPHABET));
        assertEquals("aa", generateAlias(3, ALPHABET));
        assertEquals("ab", generateAlias(4, ALPHABET));
        assertEquals("ac", generateAlias(5, ALPHABET));
        assertEquals("ba", generateAlias(6, ALPHABET));
        assertEquals("bb", generateAlias(7, ALPHABET));
        assertEquals("bc", generateAlias(8, ALPHABET));
        assertEquals("ca", generateAlias(9, ALPHABET));
        assertEquals("cb", generateAlias(10, ALPHABET));
        assertEquals("cc", generateAlias(11, ALPHABET));
        assertEquals("aaa", generateAlias(12, ALPHABET));
        assertEquals("aab", generateAlias(13, ALPHABET));
        assertEquals("aac", generateAlias(14, ALPHABET));
        assertEquals("aba", generateAlias(15, ALPHABET));
    }
}
