package com.github.mzz.exchange.mysql.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The interface Result set mapper.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
public interface ResultSetMapper<T> {

    /**
     * Map.
     *
     * @param rs the rs
     * @return the t
     * @throws SQLException the sql exception
     */
    T map(ResultSet rs) throws SQLException;
}
