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
package io.zatarox.squiggle.mock;

import io.zatarox.squiggle.statement.StatementBuilder;

import java.util.ArrayList;
import java.util.List;

// Builder for itself...
public class MockStatement implements StatementBuilder<MockStatement> {

    private final String query;
    private final List<Object> parameters = new ArrayList<Object>();

    MockStatement(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public MockStatement buildStatement() {
        return this;
    }

    @Override
    public void addBoolean(Boolean value) {
        parameters.add(value);
    }

    @Override
    public void addInteger(Integer value) {
        parameters.add(value);
    }

    @Override
    public void addString(String value) {
        parameters.add(value);
    }
}