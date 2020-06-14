package com.github.squigglesql.squigglesql.join.multiple;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

class ColorDao {

    static final Table TABLE = new Table("color");
    static final TableColumn ID = TABLE.get("id");
    static final TableColumn NAME = TABLE.get("name");

    static void insert(Connection connection, Color color) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(ID, Parameter.of(color.getId()));
        query.addValue(NAME, Parameter.of(color.getName()));
        JdbcUtils.insert(query, connection, rs -> rs.getInt(1));
    }
}
