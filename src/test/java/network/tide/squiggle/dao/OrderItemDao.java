package network.tide.squiggle.dao;

import network.tide.squiggle.Table;
import network.tide.squiggle.TableColumn;
import network.tide.squiggle.criteria.MatchCriteria;
import network.tide.squiggle.parameter.Parameter;
import network.tide.squiggle.query.InsertQuery;
import network.tide.squiggle.query.SelectQuery;
import network.tide.squiggle.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

import static network.tide.squiggle.criteria.MatchCriteria.EQUALS;

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
        int id = JdbcUtils.insert(query, connection);
        return new OrderItem(id, order, product, quantity);
    }

    public static OrderItem select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        OrderItemMapper mapper = new OrderItemMapper(query);
        query.addCriteria(new MatchCriteria(mapper.getIdRef(), EQUALS, Parameter.of(id)));
        return JdbcUtils.selectOne(query, connection, mapper);
    }
}
