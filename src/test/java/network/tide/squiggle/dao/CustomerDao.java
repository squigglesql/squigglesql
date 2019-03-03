package network.tide.squiggle.dao;

import network.tide.squiggle.Matchable;
import network.tide.squiggle.ResultMapper;
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
import java.util.List;
import java.util.function.BiConsumer;

public class CustomerDao {

    private static final Table TABLE = new Table("customer");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn NAME = TABLE.get("name");
    private static final TableColumn CITY = TABLE.get("city");

    public static ResultMapper<Customer> addToQuery(SelectQuery query, BiConsumer<TableReference, Matchable> joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn name = query.addToSelection(ref.get(NAME));
        ResultColumn city = query.addToSelection(ref.get(CITY));

        joiner.accept(ref, ref.get(ID));

        return rs -> new Customer(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readString(rs, name.getIndex()),
                JdbcUtils.readString(rs, city.getIndex()));
    }

    public static Customer insert(Connection connection, String name, String city) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(NAME, Parameter.of(name));
        query.addValue(CITY, Parameter.of(city));
        int id = JdbcUtils.insert(query, connection);
        return new Customer(id, name, city);
    }

    public static Customer select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        ResultMapper<Customer> mapper = addToQuery(query, (ref, key) -> {
            query.addCriteria(new MatchCriteria(key, MatchCriteria.EQUALS, Parameter.of(id)));
        });
        return JdbcUtils.selectOne(query, connection, mapper);
    }

    public static List<Customer> selectByCity(Connection connection, String city) throws SQLException {
        SelectQuery query = new SelectQuery();
        ResultMapper<Customer> mapper = addToQuery(query, (ref, key) -> {
            query.addCriteria(new MatchCriteria(ref.get(CITY), MatchCriteria.EQUALS, Parameter.of(city)));
            query.addOrder(ref.get(ID), true);
        });
        return JdbcUtils.selectAll(query, connection, mapper);
    }
}
