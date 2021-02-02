package com.github.mzz.exchange.spring.jdbc;

import com.github.mzz.exchange.core.model.DataContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * The type Map sql parameter source converter.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
public class MapSqlParameterSourceConverter<T> implements SqlParameterSourceConverter<T> {

    private Function<T, Map<String, ?>> mapFunction;

    public MapSqlParameterSourceConverter(Function<T, Map<String, ?>> mapFunction) {
        this.mapFunction = mapFunction;
    }

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
                .map(mapFunction)
                .map(MapSqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
    }
}
