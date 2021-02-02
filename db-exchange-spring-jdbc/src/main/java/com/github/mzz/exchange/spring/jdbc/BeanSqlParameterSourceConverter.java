package com.github.mzz.exchange.spring.jdbc;

import com.github.mzz.exchange.core.model.DataContext;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

/**
 * The type Bean sql parameter source converter.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
public class BeanSqlParameterSourceConverter<T> implements SqlParameterSourceConverter<T> {

    /**
     * Convert.
     *
     * @param dataContexts the data contexts
     * @return the sql parameter source [ ]
     */
    @Override
    public SqlParameterSource[] convert(List<DataContext<T>> dataContexts) {
        return dataContexts.stream()
                .map(DataContext::getData)
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
    }
}
