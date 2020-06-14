package com.github.squigglesql.squigglesql.join.single;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;

class ColorDao {

    static final Table TABLE = new Table("color");
    static final TableColumn ID = TABLE.get("id");
    static final TableColumn COLOR = TABLE.get("color");
}
