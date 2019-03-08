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
import static network.tide.squiggle.dao.OrderDao.*;

public class OrderMapper implements ResultMapper<Order> {

    private final TableReference ref;

    private final ResultColumn id;
    private final ResultColumn issuedAt;
    private final CustomerMapper customer;

    public OrderMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        issuedAt = query.addToSelection(ref.get(ISSUED_AT));

        customer = new CustomerMapper(query);
        query.addCriteria(new MatchCriteria(customer.getIdRef(), EQUALS, ref.get(CUSTOMER_ID)));
    }

    public Selectable getIdRef() {
        return ref.get(ID);
    }

    public Selectable getCityRef() {
        return customer.getCityRef();
    }

    @Override
    public Order apply(ResultSet rs) throws SQLException {
        return new Order(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readInstant(rs, issuedAt.getIndex()),
                customer.apply(rs));
    }
}
