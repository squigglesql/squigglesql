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
package com.github.squigglesql.squigglesql.literal;

import com.github.squigglesql.squigglesql.QueryCompiler;

/**
 * Abstract string literal. Surrounds the value with database-specific quotes and escapes the string between them by
 * replacing each quote character with two consequent quote characters.
 */
public abstract class AbstractStringLiteral extends Literal {

    @Override
    public void compile(QueryCompiler compiler) {
        char quote = compiler.getSyntax().getTextQuote();
        String str = String.valueOf(quote);
        // Note: we can't use compiler.quote method here as it fails to work with quote symbols in the input.
        compiler.write(quote).write(getValue().toString().replace(str, str + str)).write(quote);
    }

    /**
     * @return original literal value.
     */
    protected abstract Object getValue();
}
