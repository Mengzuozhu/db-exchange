package com.github.mzz.exchange.mysql.mapper;

import org.apache.commons.dbutils.BasicRowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author mengzz
 **/
public class ResultSetToMap implements ResultSetMapper<Map<String, Object>> {
    private BasicRowProcessor basicRowProcessor = new BasicRowProcessor();

    @Override
    public Map<String, Object> map(ResultSet rs) throws SQLException {
        return basicRowProcessor.toMap(rs);
    }
}
