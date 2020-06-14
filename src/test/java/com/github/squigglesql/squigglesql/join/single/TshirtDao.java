package com.github.squigglesql.squigglesql.join.single;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

class TshirtDao {

    static final Table TABLE = new Table("tshirt");
    static final TableColumn ID = TABLE.get("id");
    static final TableColumn SIZE = TABLE.get("size");
    static final TableColumn COLOR_ID = TABLE.get("color_id");

    static void insert(Connection connection, Tshirt tshirt) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(ID, Parameter.of(tshirt.getId()));
        query.addValue(SIZE, Parameter.of(tshirt.getSize()));
        query.addValue(COLOR_ID, Parameter.of(tshirt.getColorId()));
        JdbcUtils.insert(query, connection, rs -> rs.getInt(1));
    }
}
