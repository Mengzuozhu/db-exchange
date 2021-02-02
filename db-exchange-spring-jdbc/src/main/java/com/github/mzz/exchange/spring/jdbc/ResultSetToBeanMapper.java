package com.github.mzz.exchange.spring.jdbc;

import com.github.mzz.exchange.core.processor.DefaultDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Result set to bean mapper.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
@Slf4j
public class ResultSetToBeanMapper<T> extends DefaultDataMapper<ResultSet, T> {
    private BeanPropertyRowMapper<T> rowMapper;

    public ResultSetToBeanMapper(Class<T> mappedClass) {
        this.rowMapper = BeanPropertyRowMapper.newInstance(mappedClass);
    }

    public static <T> ResultSetToBeanMapper<T> of(Class<T> mappedClass) {
        return new ResultSetToBeanMapper<>(mappedClass);
    }

    @Override
    protected T map(ResultSet data) {
        T res = null;
        try {
            res = rowMapper.mapRow(data, 0);
        } catch (SQLException e) {
            log.error("map fail", e);
        }
        return res;
    }
}
