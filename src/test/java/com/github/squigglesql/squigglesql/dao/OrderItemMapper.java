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

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.Selectable;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.squigglesql.squigglesql.criteria.Criteria.equal;
import static com.github.squigglesql.squigglesql.dao.OrderItemDao.*;

public class OrderItemMapper implements ResultMapper<OrderItem> {

    private final TableReference ref;

    private final ResultColumn id;
    private final OrderMapper order;
    private final ProductMapper product;
    private final ResultColumn quantity;

    public OrderItemMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        quantity = query.addToSelection(ref.get(QUANTITY));

        order = new OrderMapper(query);
        query.addCriteria(equal(order.getIdRef(), ref.get(ORDER_ID)));

        product = new ProductMapper(query);
        query.addCriteria(equal(product.getIdRef(), ref.get(PRODUCT_ID)));
    }

    public Selectable getIdRef() {
        return ref.get(ID);
    }

    @Override
    public OrderItem apply(ResultSet rs) throws SQLException {
        return new OrderItem(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                order.apply(rs),
                product.apply(rs),
                JdbcUtils.readIntegerNotNull(rs, quantity.getIndex()));
    }
}
