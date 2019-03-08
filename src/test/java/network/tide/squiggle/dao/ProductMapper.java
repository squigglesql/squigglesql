package network.tide.squiggle.dao;

import network.tide.squiggle.ResultMapper;
import network.tide.squiggle.Selectable;
import network.tide.squiggle.TableReference;
import network.tide.squiggle.query.ResultColumn;
import network.tide.squiggle.query.SelectQuery;
import network.tide.squiggle.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static network.tide.squiggle.dao.ProductDao.*;

public class ProductMapper implements ResultMapper<Product> {

    private final TableReference ref;

    private final ResultColumn id;
    private final ResultColumn name;
    private final ResultColumn price;

    public ProductMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        name = query.addToSelection(ref.get(NAME));
        price = query.addToSelection(ref.get(PRICE));
    }

    public Selectable getIdRef() {
        return ref.get(ID);
    }

    @Override
    public Product apply(ResultSet rs) throws SQLException {
        return new Product(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readString(rs, name.getIndex()),
                JdbcUtils.readIntegerNotNull(rs, price.getIndex()));
    }
}
