package com.github.mzz.exchange.spring.jdbc;

import com.github.mzz.exchange.core.model.DataContext;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

/**
 * The interface Sql parameter source converter.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
public interface SqlParameterSourceConverter<T> {

    /**
     * Convert.
     *
     * @param dataContexts the data contexts
     * @return the sql parameter source [ ]
     */
    SqlParameterSource[] convert(List<DataContext<T>> dataContexts);

}
