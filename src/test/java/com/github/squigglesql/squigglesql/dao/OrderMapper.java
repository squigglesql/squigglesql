package com.github.squigglesql.squigglesql.dao;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.Selectable;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.criteria.MatchCriteria;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.squigglesql.squigglesql.criteria.MatchCriteria.EQUALS;
import static com.github.squigglesql.squigglesql.dao.OrderDao.*;

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
