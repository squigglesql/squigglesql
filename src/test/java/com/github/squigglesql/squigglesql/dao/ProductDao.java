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
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class ProductDao {

    static final Table TABLE = new Table("product");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn NAME = TABLE.get("name");
    static final TableColumn PRICE = TABLE.get("price");

    public static Product insert(Connection connection, String name, int price) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(NAME, Parameter.of(name));
        query.addValue(PRICE, Parameter.of(price));
        int id = JdbcUtils.insert(query, connection);
        return new Product(id, name, price);
    }
}
