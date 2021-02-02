package com.github.mzz.exchange.mysql.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author mengzz
 **/
public class MapPreparedStatementSetter<T> implements PreparedStatementSetter<Map<Integer, T>> {

    @Override
    public void fillStatement(PreparedStatement preparedStatement, Map<Integer, T> inputs) throws SQLException {
        for (Map.Entry<Integer, T> entry : inputs.entrySet()) {
            preparedStatement.setObject(entry.getKey(), entry.getValue());
        }
    }
}
