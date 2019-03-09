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
