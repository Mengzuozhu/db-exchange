package com.github.mzz.exchange.mysql.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author mengzz
 **/
public class ListPreparedStatementSetter<T> implements PreparedStatementSetter<List<T>> {

    @Override
    public void fillStatement(PreparedStatement preparedStatement, List<T> inputs) throws SQLException {
        for (int i = 0; i < inputs.size(); i++) {
            preparedStatement.setObject(i + 1, inputs.get(i));
        }
    }
}
