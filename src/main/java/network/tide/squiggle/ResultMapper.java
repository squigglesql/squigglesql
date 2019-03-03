package network.tide.squiggle;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultMapper<T> {

    T apply(ResultSet rs) throws SQLException;
}
