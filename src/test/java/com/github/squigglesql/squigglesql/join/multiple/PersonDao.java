package com.github.squigglesql.squigglesql.join.multiple;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

class PersonDao {

    static final Table TABLE = new Table("person");
    static final TableColumn ID = TABLE.get("id");
    static final TableColumn LAST_NAME = TABLE.get("last_name");

    static void insert(Connection connection, Person person) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(ID, Parameter.of(person.getId()));
        query.addValue(LAST_NAME, Parameter.of(person.getLastName()));
        JdbcUtils.insert(query, connection, rs -> rs.getInt(1));
    }
}
