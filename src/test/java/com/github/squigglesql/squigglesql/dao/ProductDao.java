package com.github.squigglesql.squigglesql.dao;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.Selectable;
import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ProductDao {

    private static final Table TABLE = new Table("product");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn NAME = TABLE.get("name");
    private static final TableColumn PRICE = TABLE.get("price");

    public interface Joiner {

        void accept(Selectable idRef);
    }

    public static ResultMapper<Product> addToQuery(SelectQuery query, Joiner joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn name = query.addToSelection(ref.get(NAME));
        ResultColumn price = query.addToSelection(ref.get(PRICE));

        joiner.accept(ref.get(ID));

        return rs -> new Product(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readString(rs, name.getIndex()),
                JdbcUtils.readIntegerNotNull(rs, price.getIndex()));
    }

    public static Product insert(Connection connection, String name, int price) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(NAME, Parameter.of(name));
        query.addValue(PRICE, Parameter.of(price));
        int id = JdbcUtils.insert(query, connection);
        return new Product(id, name, price);
    }
}
