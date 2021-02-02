package com.github.mzz.exchange.mysql.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The interface Prepared statement setter.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
public interface PreparedStatementSetter<T> {

    /**
     * Fill statement.
     *
     * @param preparedStatement the prepared statement
     * @param inputs             the inputs
     * @throws SQLException the sql exception
     */
    void fillStatement(PreparedStatement preparedStatement, T inputs) throws SQLException;

}
