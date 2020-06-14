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
package com.github.squigglesql.squigglesql.join.multiple;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

class RecordMapper implements ResultMapper<Record> {

    private final TableReference vehicleRef;
    private final TableReference colorRef;
    private final TableReference personRef;

    private final ResultColumn vehicleName;
    private final ResultColumn colorName;
    private final ResultColumn lastName;

    RecordMapper(SelectQuery query) {
        vehicleRef = VehicleDao.TABLE.refer();
        colorRef = ColorDao.TABLE.refer();
        personRef = PersonDao.TABLE.refer();

        vehicleName = query.addToSelection(vehicleRef.get(VehicleDao.NAME));
        colorName = query.addToSelection(colorRef.get(ColorDao.NAME));
        lastName = query.addToSelection(personRef.get(PersonDao.LAST_NAME));
    }

    TableReference getVehicleRef() {
        return vehicleRef;
    }

    TableReference getColorRef() {
        return colorRef;
    }

    TableReference getPersonRef() {
        return personRef;
    }

    @Override
    public Record apply(ResultSet rs) throws SQLException {
        return new Record(
                JdbcUtils.readString(rs, vehicleName.getIndex()),
                JdbcUtils.readString(rs, colorName.getIndex()),
                JdbcUtils.readString(rs, lastName.getIndex()));
    }
}
