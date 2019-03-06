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

public abstract class CustomerDao {

    private static final Table TABLE = new Table("customer");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn NAME = TABLE.get("name");
    private static final TableColumn CITY = TABLE.get("city");

    public interface Joiner {

        void accept(Selectable idRef);
    }

    public static ResultMapper<Customer> addToQuery(SelectQuery query, Joiner joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn name = query.addToSelection(ref.get(NAME));
        ResultColumn city = query.addToSelection(ref.get(CITY));

        joiner.accept(ref.get(ID));

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
        ResultMapper<Customer> mapper = addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, MatchCriteria.EQUALS, Parameter.of(id)));
        });
        return JdbcUtils.selectOne(query, connection, mapper);
    }
}
