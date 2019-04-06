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

/**
 * Raw (unsafe) SQL literal. If you add it to a query, the compiler will simply dump <tt>sql</tt> parameter
 * value as a part of the output. Be careful when you use this kind of literal. Use
 * {@link Literal#unsafe(String)} method to instantiate it.
 */
public class RawLiteral extends Literal {

    /**
     * Null literal. Returned as a result of {@link Literal#ofNull()} method or any <tt>of</tt> method with
     * <tt>null</tt> in parameter.
     */
    static final Literal NULL = new RawLiteral(null);

    /**
     * True boolean literal. Returns as a result of {@link Literal#of(boolean)} method with <tt>true</tt> in parameter.
     */
    static final Literal TRUE = new RawLiteral(true);

    /**
     * False boolean literal. Returns as a result of {@link Literal#of(boolean)} method with <tt>false</tt> in parameter.
     */
    static final Literal FALSE = new RawLiteral(false);

    private final Object sql;

    /**
     * Creates a literal.
     *
     * @param sql part of SQL query to dump as a literal.
     */
    RawLiteral(Object sql) {
        this.sql = sql;
    }

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write(String.valueOf(sql));
    }
}
