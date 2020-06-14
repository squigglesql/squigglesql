package com.github.squigglesql.squigglesql.join.single;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.query.ResultColumn;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import com.github.squigglesql.squigglesql.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.squigglesql.squigglesql.join.single.ColorDao.*;

class ColorMapper implements ResultMapper<Color> {

    private final TableReference ref;

    private final ResultColumn id;
    private final ResultColumn color;

    ColorMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        color = query.addToSelection(ref.get(COLOR));
    }

    TableReference getRef() {
        return ref;
    }

    @Override
    public Color apply(ResultSet rs) throws SQLException {
        Integer id = JdbcUtils.readIntegerNull(rs, this.id.getIndex());
        return id == null ? null : new Color(id,
                JdbcUtils.readString(rs, color.getIndex()));
    }
}
