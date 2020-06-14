package com.github.squigglesql.squigglesql.join.single;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.query.SelectQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

class RecordMapper implements ResultMapper<Record> {

    private final TshirtMapper tshirtMapper;
    private final ColorMapper colorMapper;

    RecordMapper(SelectQuery query) {
        tshirtMapper = new TshirtMapper(query);
        colorMapper = new ColorMapper(query);
    }

    TableReference getTshirtRef() {
        return tshirtMapper.getRef();
    }

    TableReference getColorRef() {
        return colorMapper.getRef();
    }

    @Override
    public Record apply(ResultSet rs) throws SQLException {
        return new Record(tshirtMapper.apply(rs), colorMapper.apply(rs));
    }
}
