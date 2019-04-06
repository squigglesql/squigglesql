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
package com.github.squigglesql.squigglesql.parameter;

import com.github.squigglesql.squigglesql.statement.Parametrized;

import java.sql.SQLException;
import java.sql.Types;

class NullParameter extends Parameter {

    static final Parameter BOOLEAN = new NullParameter(Types.BOOLEAN);
    static final Parameter BYTE = new NullParameter(Types.TINYINT);
    static final Parameter SHORT = new NullParameter(Types.SMALLINT);
    static final Parameter INTEGER = new NullParameter(Types.INTEGER);
    static final Parameter LONG = new NullParameter(Types.BIGINT);
    static final Parameter FLOAT = new NullParameter(Types.REAL);
    static final Parameter DOUBLE = new NullParameter(Types.DOUBLE);
    static final Parameter TIMESTAMP = new NullParameter(Types.TIMESTAMP);
    static final Parameter TIMESTAMP_WITH_TIMEZONE = new NullParameter(Types.TIMESTAMP_WITH_TIMEZONE);
    static final Parameter DATE = new NullParameter(Types.DATE);

    private final int sqlType;

    NullParameter(int sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public void addValue(Parametrized statement) throws SQLException {
        statement.addNull(sqlType);
    }
}
