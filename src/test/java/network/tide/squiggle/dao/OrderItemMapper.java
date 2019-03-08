package network.tide.squiggle.dao;

import network.tide.squiggle.ResultMapper;
import network.tide.squiggle.Selectable;
import network.tide.squiggle.TableReference;
import network.tide.squiggle.criteria.MatchCriteria;
import network.tide.squiggle.query.ResultColumn;
import network.tide.squiggle.query.SelectQuery;
import network.tide.squiggle.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static network.tide.squiggle.criteria.MatchCriteria.EQUALS;
import static network.tide.squiggle.dao.OrderItemDao.*;

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
        query.addCriteria(new MatchCriteria(order.getIdRef(), EQUALS, ref.get(ORDER_ID)));

        product = new ProductMapper(query);
        query.addCriteria(new MatchCriteria(product.getIdRef(), EQUALS, ref.get(PRODUCT_ID)));
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
