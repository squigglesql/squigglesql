/*
 * Copyright 2019-2020 Egor Nepomnyaschih.
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
import com.github.squigglesql.squigglesql.query.SelectQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

class RecordMapper implements ResultMapper<Record> {

    private final TshirtMapper tshirtMapper;
    private final ColorMapper colorMapper;

    RecordMapper(SelectQuery query) {
        tshirtMapper = new TshirtMapper(query);
        colorMapper = new ColorMapper(query);
    }

    TableReference getTshirtRef() {
        return tshirtMapper.getRef();
    }

    TableReference getColorRef() {
        return colorMapper.getRef();
    }

    @Override
    public Record apply(ResultSet rs) throws SQLException {
        return new Record(tshirtMapper.apply(rs), colorMapper.apply(rs));
    }
}
