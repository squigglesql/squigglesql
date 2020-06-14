/*
 * Copyright 2020 Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.join.single;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.squigglesql.squigglesql.join.single.TshirtDao.*;

class TshirtMapper implements ResultMapper<Tshirt> {

    private final TableReference ref;

    private final ResultColumn id;
    private final ResultColumn size;
    private final ResultColumn colorId;

    TshirtMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        size = query.addToSelection(ref.get(SIZE));
        colorId = query.addToSelection(ref.get(COLOR_ID));
    }

    TableReference getRef() {
        return ref;
    }

    @Override
    public Tshirt apply(ResultSet rs) throws SQLException {
        Integer id = JdbcUtils.readIntegerNull(rs, this.id.getIndex());
        return id == null ? null : new Tshirt(id,
                JdbcUtils.readString(rs, size.getIndex()),
                JdbcUtils.readIntegerNull(rs, colorId.getIndex()));
    }
}
