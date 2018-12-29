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
package io.zatarox.squiggle;

import io.zatarox.squiggle.output.Output;
import io.zatarox.squiggle.output.Outputable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Query implements Outputable {

    public static final String DEFAULT_INDENT = "    ";

    @Override
    public String toString() {
        return toString(DEFAULT_INDENT);
    }

    public String toString(String indent) {
        return compile(indent).toString();
    }

    public PreparedStatement toStatement(Connection connection) throws SQLException {
        return compile(DEFAULT_INDENT).toStatement(connection);
    }

    private Output compile(String indent) {
        Output out = new Output(indent);
        write(out);
        return out;
    }
}
