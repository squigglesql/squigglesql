package com.github.squigglesql.squigglesql.join.multiple;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.parameter.Parameter;
import com.github.squigglesql.squigglesql.query.InsertQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

class VehicleDao {

    static final Table TABLE = new Table("vehicle");
    static final TableColumn ID = TABLE.get("id");
    static final TableColumn NAME = TABLE.get("name");
    static final TableColumn COLOR_ID = TABLE.get("color_id");
    static final TableColumn PERSON_ID = TABLE.get("person_id");

    static void insert(Connection connection, Vehicle vehicle) throws SQLException {
        InsertQuery query = new InsertQuery(TABLE);
        query.addValue(ID, Parameter.of(vehicle.getId()));
        query.addValue(NAME, Parameter.of(vehicle.getName()));
        query.addValue(COLOR_ID, Parameter.of(vehicle.getColorId()));
        query.addValue(PERSON_ID, Parameter.of(vehicle.getPersonId()));
        JdbcUtils.insert(query, connection, rs -> rs.getInt(1));
    }
}
