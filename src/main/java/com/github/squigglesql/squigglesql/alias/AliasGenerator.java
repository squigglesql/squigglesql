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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Unified alias generator. It generates new aliases by auto-incrementing short sequences of symbols in scope of
 * the specified alphabet. It remembers the previously generated aliases to make sure that they are unique. It first
 * tries to apply the preferred aliases of an object the alias is being generated for.
 */
public class AliasGenerator {

    private final Alphabet alphabet;
    private final Set<String> usedAliases = new HashSet<>();
    private int nextAlphabeticIndex = 0;

    /**
     * Creates an alias generator.
     *
     * @param alphabet alphabet to stick to.
     */
    public AliasGenerator(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * Generates a unique alias for an object. First tries to apply the preferred aliases. If they are occupied,
     * generates an alias by auto-incrementing.
     *
     * @param aliasable object to generate an alias for.
     * @return new alias for the object.
     */
    public String generateAlias(Aliasable aliasable) {
        for (String preferredAlias : aliasable.getPreferredAliases()) {
            if (!usedAliases.contains(preferredAlias)) {
                usedAliases.add(preferredAlias);
                return preferredAlias;
            }
        }
        while (true) {
            String alphabeticAlias = generateAlphabetic(nextAlphabeticIndex++, alphabet);
            if (!usedAliases.contains(alphabeticAlias)) {
                usedAliases.add(alphabeticAlias);
                return alphabeticAlias;
            }
        }
    }

    /**
     * Generates unique aliases for objects. The objects must be viable as keys of a hash map.
     *
     * @param collection objects to generate aliases for.
     * @param <T>        type of an object.
     * @return map from an object to its alias.
     */
    private <T extends Aliasable> Map<T, String> generateAliases(Collection<? extends T> collection) {
        Map<T, String> aliases = new HashMap<>();
        for (T item : collection) {
            aliases.put(item, generateAlias(item));
        }
        return aliases;
    }

    /**
     * Generates an auto-incremented alias in scope of an alphabet.
     *
     * @param index    0-based index in auto-incremental sequence.
     * @param alphabet alphabet to stick to.
     * @return alias.
     */
    public static String generateAlphabetic(int index, Alphabet alphabet) {
        StringBuilder result = new StringBuilder();
        while (index >= 0) {
            result.insert(0, (char) (alphabet.getFirstLetter() + index % alphabet.getLetterCount()));
            index = index / alphabet.getLetterCount() - 1;
        }
        return result.toString();
    }

    /**
     * Generates unique aliases for objects. The objects must be viable as keys of a hash map. The method doesn't have
     * a history, i.e. it is impossible to generate more unique aliases after its call. If you need a history,
     * construct an {@link AliasGenerator} instance and call its {@link AliasGenerator#generateAliases(Collection)}
     * method.
     *
     * @param collection objects to generate aliases for.
     * @param alphabet   alphabet to stick to.
     * @param <T>        type of an object.
     * @return map from an object to its alias.
     */
    public static <T extends Aliasable> Map<T, String> generateAliases(
            Collection<? extends T> collection, Alphabet alphabet) {
        return new AliasGenerator(alphabet).generateAliases(collection);
    }
}
