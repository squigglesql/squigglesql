/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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

public class RawLiteral extends Literal {

    static final Literal NULL = new RawLiteral(null);
    static final Literal TRUE = new RawLiteral(true);
    static final Literal FALSE = new RawLiteral(false);

    private final Object value;

    RawLiteral(Object value) {
        this.value = value;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(String.valueOf(value));
    }
}
