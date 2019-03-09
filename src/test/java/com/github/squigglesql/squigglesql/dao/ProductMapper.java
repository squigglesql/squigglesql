package com.github.squigglesql.squigglesql.dao;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.Selectable;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.squigglesql.squigglesql.dao.ProductDao.*;

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
