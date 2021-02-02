package com.github.mzz.exchange.mysql.mapper;

import org.apache.commons.dbutils.BasicRowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mengzz
 **/
public class ResultSetToBean<T> implements ResultSetMapper<T> {
    private final Class<? extends T> type;
    private BasicRowProcessor basicRowProcessor = new BasicRowProcessor();

    public ResultSetToBean(Class<? extends T> type) {
        this.type = type;
    }

    @Override
    public T map(ResultSet rs) throws SQLException {
        return basicRowProcessor.toBean(rs, type);
    }
}
