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

import static com.github.squigglesql.squigglesql.criteria.Criteria.equal;

public class OrderItemDao {

    static final Table TABLE = new Table("order_item");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn ORDER_ID = TABLE.get("order_id");
    static final TableColumn PRODUCT_ID = TABLE.get("product_id");
    static final TableColumn QUANTITY = TABLE.get("quantity");

    public static OrderItem insert(Connection connection, Order order, Product product, int quantity) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(ORDER_ID, Parameter.of(order.getId()));
        query.addValue(PRODUCT_ID, Parameter.of(product.getId()));
        query.addValue(QUANTITY, Parameter.of(quantity));
        int id = JdbcUtils.insert(query, connection, rs -> rs.getInt(1));
        return new OrderItem(id, order, product, quantity);
    }

    public static OrderItem select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        OrderItemMapper mapper = new OrderItemMapper(query);
        query.addCriteria(equal(mapper.getIdRef(), Parameter.of(id)));
        return JdbcUtils.selectOne(query, connection, mapper);
    }
}
