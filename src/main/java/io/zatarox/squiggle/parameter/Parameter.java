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
package io.zatarox.squiggle.parameter;

import io.zatarox.squiggle.Matchable;
import io.zatarox.squiggle.QueryCompiler;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.statement.Parametrized;

import java.sql.SQLException;
import java.util.Set;

public abstract class Parameter implements Matchable {

    @Override
    public void compile(QueryCompiler compiler) {
        compiler.write("?").addParameter(this);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
    }

    public abstract void addValue(Parametrized builder) throws SQLException;

    // We don't provide "of" methods, because null value would have ambiguous type, while it is crucial for JDBC.
}
