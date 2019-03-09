package com.github.squigglesql.squigglesql.dao;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.Selectable;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.squigglesql.squigglesql.dao.CustomerDao.*;

public class CustomerMapper implements ResultMapper<Customer> {

    private final TableReference ref;

    private final ResultColumn id;
    private final ResultColumn name;
    private final ResultColumn city;

    public CustomerMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        name = query.addToSelection(ref.get(NAME));
        city = query.addToSelection(ref.get(CITY));
    }

    public Selectable getIdRef() {
        return ref.get(ID);
    }

    public Selectable getCityRef() {
        return ref.get(CITY);
    }

    @Override
    public Customer apply(ResultSet rs) throws SQLException {
        return new Customer(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readString(rs, name.getIndex()),
                JdbcUtils.readString(rs, city.getIndex()));
    }
}
