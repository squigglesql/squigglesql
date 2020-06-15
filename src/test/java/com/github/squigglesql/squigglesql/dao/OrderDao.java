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
package com.github.squigglesql.squigglesql.dao;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static com.github.squigglesql.squigglesql.criteria.Criteria.equal;

public class OrderDao {

    static final Table TABLE = new Table("order");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn ISSUED_AT = TABLE.get("issued_at");
    static final TableColumn CUSTOMER_ID = TABLE.get("customer_id");

    public static Order insert(Connection connection, Instant issuedAt, Customer customer) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(ISSUED_AT, Parameter.of(issuedAt));
        query.addValue(CUSTOMER_ID, Parameter.of(customer.getId()));
        int id = JdbcUtils.insert(query, connection, rs -> rs.getInt(1));
        return new Order(id, issuedAt, customer);
    }

    public static Order select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        OrderMapper mapper = new OrderMapper(query);
        query.addCriteria(equal(mapper.getIdRef(), Parameter.of(id)));
        return JdbcUtils.selectOne(query, connection, mapper);
    }

    public static List<Order> selectByCity(Connection connection, String city) throws SQLException {
        SelectQuery query = new SelectQuery();
        OrderMapper mapper = new OrderMapper(query);
        query.addCriteria(equal(mapper.getCityRef(), Parameter.of(city)));
        query.addOrder(mapper.getIdRef(), true);
        return JdbcUtils.selectAll(query, connection, mapper);
    }
}
