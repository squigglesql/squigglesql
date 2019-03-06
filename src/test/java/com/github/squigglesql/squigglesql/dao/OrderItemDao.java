package com.github.squigglesql.squigglesql.dao;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.Selectable;
import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.criteria.MatchCriteria;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

import static com.github.squigglesql.squigglesql.criteria.MatchCriteria.EQUALS;

public abstract class OrderItemDao {

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

        ResultMapper<Order> order = OrderDao.addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, ref.get(ORDER_ID)));
        });

        ResultMapper<Product> product = ProductDao.addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, ref.get(PRODUCT_ID)));
        });

        joiner.accept(ref.get(ID));

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
