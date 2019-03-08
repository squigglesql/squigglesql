package network.tide.squiggle.dao;

import network.tide.squiggle.Table;
import network.tide.squiggle.TableColumn;
import network.tide.squiggle.parameter.Parameter;
import network.tide.squiggle.query.InsertQuery;
import network.tide.squiggle.util.JdbcUtils;

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
