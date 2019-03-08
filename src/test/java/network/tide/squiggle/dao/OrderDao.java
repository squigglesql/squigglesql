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
import java.time.Instant;
import java.util.List;

import static network.tide.squiggle.criteria.MatchCriteria.EQUALS;

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
        return JdbcUtils.selectAll(query, connection, mapper);
    }
}
