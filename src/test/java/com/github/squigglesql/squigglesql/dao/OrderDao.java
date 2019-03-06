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
import java.time.Instant;

import static com.github.squigglesql.squigglesql.criteria.MatchCriteria.EQUALS;

public abstract class OrderDao {

    private static final Table TABLE = new Table("order");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn ISSUED_AT = TABLE.get("issued_at");
    private static final TableColumn CUSTOMER_ID = TABLE.get("customer_id");

    public interface Joiner {

        void accept(Selectable idRef);
    }

    public static ResultMapper<Order> addToQuery(SelectQuery query, Joiner joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn issuedAt = query.addToSelection(ref.get(ISSUED_AT));

        ResultMapper<Customer> customer = CustomerDao.addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, ref.get(CUSTOMER_ID)));
        });

        joiner.accept(ref.get(ID));

        return rs -> new Order(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readInstant(rs, issuedAt.getIndex()),
                customer.apply(rs));
    }

    public static Order insert(Connection connection, Instant issuedAt, Customer customer) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(ISSUED_AT, Parameter.of(issuedAt));
        query.addValue(CUSTOMER_ID, Parameter.of(customer.getId()));
        int id = JdbcUtils.insert(query, connection);
        return new Order(id, issuedAt, customer);
    }

    public static Order select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        ResultMapper<Order> mapper = addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, MatchCriteria.EQUALS, Parameter.of(id)));
        });
        return JdbcUtils.selectOne(query, connection, mapper);
    }
}
