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
import java.time.Instant;
import java.util.List;

import static network.tide.squiggle.criteria.MatchCriteria.EQUALS;

public class OrderDao {

    private static final Table TABLE = new Table("order");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn ISSUED_AT = TABLE.get("issued_at");
    private static final TableColumn CUSTOMER_ID = TABLE.get("customer_id");

    public interface Joiner {

        void accept(Selectable idRef, Selectable cityRef);
    }

    public static ResultMapper<Order> addToQuery(SelectQuery query, Joiner joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn issuedAt = query.addToSelection(ref.get(ISSUED_AT));

        ResultMapper<Customer> customer = CustomerDao.addToQuery(query, (idRef, cityRef) -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, ref.get(CUSTOMER_ID)));
            joiner.accept(ref.get(ID), cityRef);
        });

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
        ResultMapper<Order> mapper = addToQuery(query, (idRef, cityRef) -> {
            query.addCriteria(new MatchCriteria(idRef, MatchCriteria.EQUALS, Parameter.of(id)));
        });
        return JdbcUtils.selectOne(query, connection, mapper);
    }

    public static List<Order> selectByCity(Connection connection, String city) throws SQLException {
        SelectQuery query = new SelectQuery();
        ResultMapper<Order> mapper = addToQuery(query, (idRef, cityRef) -> {
            query.addCriteria(new MatchCriteria(cityRef, MatchCriteria.EQUALS, Parameter.of(city)));
        });
        return JdbcUtils.selectAll(query, connection, mapper);
    }
}
