package com.github.squigglesql.squigglesql.join.single;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;

class TshirtDao {

    static final Table TABLE = new Table("tshirt");
    static final TableColumn ID = TABLE.get("id");
    static final TableColumn SIZE = TABLE.get("size");
    static final TableColumn COLOR_ID = TABLE.get("color_id");
}
