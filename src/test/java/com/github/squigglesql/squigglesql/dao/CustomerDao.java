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
import java.util.List;

import static com.github.squigglesql.squigglesql.criteria.MatchCriteria.EQUALS;

public class CustomerDao {

    static final Table TABLE = new Table("customer");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn NAME = TABLE.get("name");
    static final TableColumn CITY = TABLE.get("city");

    public static Customer insert(Connection connection, String name, String city) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(NAME, Parameter.of(name));
        query.addValue(CITY, Parameter.of(city));
        int id = JdbcUtils.insert(query, connection);
        return new Customer(id, name, city);
    }

    public static Customer select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        CustomerMapper mapper = new CustomerMapper(query);
        query.addCriteria(new MatchCriteria(mapper.getIdRef(), EQUALS, Parameter.of(id)));
        return JdbcUtils.selectOne(query, connection, mapper);
    }

    public static List<Customer> selectByCity(Connection connection, String city) throws SQLException {
        SelectQuery query = new SelectQuery();
        CustomerMapper mapper = new CustomerMapper(query);
        query.addCriteria(new MatchCriteria(mapper.getCityRef(), EQUALS, Parameter.of(city)));
        query.addOrder(mapper.getIdRef(), true);
        return JdbcUtils.selectAll(query, connection, mapper);
    }
}
