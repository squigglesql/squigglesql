package network.tide.squiggle.dao;

import network.tide.squiggle.ResultMapper;
import network.tide.squiggle.Selectable;
import network.tide.squiggle.TableReference;
import network.tide.squiggle.query.ResultColumn;
import network.tide.squiggle.query.SelectQuery;
import network.tide.squiggle.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static network.tide.squiggle.dao.CustomerDao.*;

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
