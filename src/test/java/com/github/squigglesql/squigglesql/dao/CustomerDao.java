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
package com.github.squigglesql.squigglesql.dao;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.github.squigglesql.squigglesql.criteria.Criteria.equal;

public class CustomerDao {

    static final Table TABLE = new Table("customer");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn NAME = TABLE.get("name");
    static final TableColumn CITY = TABLE.get("city");

    public static Customer insert(Connection connection, String name, String city) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(NAME, Parameter.of(name));
        query.addValue(CITY, Parameter.of(city));
        int id = JdbcUtils.insert(query, connection, rs -> rs.getInt(1));
        return new Customer(id, name, city);
    }

    public static Customer select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        CustomerMapper mapper = new CustomerMapper(query);
        query.addCriteria(equal(mapper.getIdRef(), Parameter.of(id)));
        return JdbcUtils.selectOne(query, connection, mapper);
    }

    public static List<Customer> selectByCity(Connection connection, String city) throws SQLException {
        SelectQuery query = new SelectQuery();
        CustomerMapper mapper = new CustomerMapper(query);
        query.addCriteria(equal(mapper.getCityRef(), Parameter.of(city)));
        query.addOrder(mapper.getIdRef(), true);
        return JdbcUtils.selectAll(query, connection, mapper);
    }
}
