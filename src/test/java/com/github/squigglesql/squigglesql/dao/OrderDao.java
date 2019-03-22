package com.github.squigglesql.squigglesql.dao;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.criteria.MatchCriteria;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static com.github.squigglesql.squigglesql.criteria.MatchCriteria.EQUALS;

public class OrderDao {

    static final Table TABLE = new Table("order");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn ISSUED_AT = TABLE.get("issued_at");
    static final TableColumn CUSTOMER_ID = TABLE.get("customer_id");

    public static Order insert(Connection connection, Instant issuedAt, Customer customer) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(ISSUED_AT, Parameter.of(issuedAt));
        query.addValue(CUSTOMER_ID, Parameter.of(customer.getId()));
        int id = JdbcUtils.insert(query, connection);
        return new Order(id, issuedAt, customer);
    }

    public static Order select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        OrderMapper mapper = new OrderMapper(query);
        query.addCriteria(new MatchCriteria(mapper.getIdRef(), EQUALS, Parameter.of(id)));
        return JdbcUtils.selectOne(query, connection, mapper);
    }

    public static List<Order> selectByCity(Connection connection, String city) throws SQLException {
        SelectQuery query = new SelectQuery();
        OrderMapper mapper = new OrderMapper(query);
        query.addCriteria(new MatchCriteria(mapper.getCityRef(), EQUALS, Parameter.of(city)));
        query.addOrder(mapper.getIdRef(), true);
        return JdbcUtils.selectAll(query, connection, mapper);
    }
}
