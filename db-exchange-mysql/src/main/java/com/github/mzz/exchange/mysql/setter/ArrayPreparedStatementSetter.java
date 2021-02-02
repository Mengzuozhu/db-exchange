package com.github.mzz.exchange.mysql.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author mengzz
 **/
public class ArrayPreparedStatementSetter implements PreparedStatementSetter<Object[]> {

    @Override
    public void fillStatement(PreparedStatement preparedStatement, Object[] inputs) throws SQLException {
        for (int i = 0; i < inputs.length; i++) {
            preparedStatement.setObject(i + 1, inputs[i]);
        }
    }
}
