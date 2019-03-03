package network.tide.squiggle.dao;

import network.tide.squiggle.ResultMapper;
import network.tide.squiggle.Selectable;
import network.tide.squiggle.Table;
import network.tide.squiggle.TableColumn;
import network.tide.squiggle.TableReference;
import network.tide.squiggle.criteria.MatchCriteria;
import network.tide.squiggle.parameter.Parameter;
import network.tide.squiggle.query.InsertQuery;
import network.tide.squiggle.query.ResultColumn;
import network.tide.squiggle.query.SelectQuery;
import network.tide.squiggle.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

import static network.tide.squiggle.criteria.MatchCriteria.EQUALS;

public class OrderItemDao {

    private static final Table TABLE = new Table("order_item");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn ORDER_ID = TABLE.get("order_id");
    private static final TableColumn PRODUCT_ID = TABLE.get("product_id");
    private static final TableColumn QUANTITY = TABLE.get("quantity");

    public interface Joiner {

        void accept(Selectable idRef);
    }

    public static ResultMapper<OrderItem> addToQuery(SelectQuery query, Joiner joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn quantity = query.addToSelection(ref.get(QUANTITY));

        ResultMapper<Order> order = OrderDao.addToQuery(query, (idRef, cityRef) -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, ref.get(ORDER_ID)));
            joiner.accept(ref.get(ID));
        });

        ResultMapper<Product> product = ProductDao.addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, ref.get(PRODUCT_ID)));
        });

        return rs -> new OrderItem(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                order.apply(rs),
                product.apply(rs),
                JdbcUtils.readIntegerNotNull(rs, quantity.getIndex()));
    }

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
        ResultMapper<OrderItem> mapper = addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, Parameter.of(id)));
        });
        return JdbcUtils.selectOne(query, connection, mapper);
    }
}
