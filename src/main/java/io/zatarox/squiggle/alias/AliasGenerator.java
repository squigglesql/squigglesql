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
package io.zatarox.squiggle.alias;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AliasGenerator {

    private final Alphabet alphabet;
    private final Set<String> usedAliases;
    private int nextAlphabeticIndex = 0;

    public AliasGenerator(Alphabet alphabet) {
        this(alphabet, new HashSet<String>());
    }

    public AliasGenerator(Alphabet alphabet, Set<String> usedAliases) {
        this.alphabet = alphabet;
        this.usedAliases = usedAliases;
    }

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

    public <T extends Aliasable> Map<T, String> generateAliases(Collection<? extends T> collection) {
        Map<T, String> aliases = new HashMap<T, String>();
        for (T item : collection) {
            aliases.put(item, generateAlias(item));
        }
        return aliases;
    }

    public static String generateAlphabetic(int index, Alphabet alphabet) {
        StringBuilder result = new StringBuilder();
        while (index >= 0) {
            result.insert(0, (char) (alphabet.getFirstLetter() + index % alphabet.getLetterCount()));
            index = index / alphabet.getLetterCount() - 1;
        }
        return result.toString();
    }

    public static <T extends Aliasable> Map<T, String> generateAliases(
            Collection<? extends T> collection, Alphabet alphabet) {
        return new AliasGenerator(alphabet).generateAliases(collection);
    }
}
