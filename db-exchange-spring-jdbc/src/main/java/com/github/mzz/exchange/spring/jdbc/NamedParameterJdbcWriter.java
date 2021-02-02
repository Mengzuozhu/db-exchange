package com.github.mzz.exchange.spring.jdbc;

import com.github.mzz.exchange.core.model.DataContext;
import com.github.mzz.exchange.core.writer.DataWriter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;

/**
 * The type Named parameter jdbc writer.
 *
 * @param <T> the type parameter
 * @author mengzz
 * @see NamedParameterJdbcTemplate
 */
public class NamedParameterJdbcWriter<T> implements DataWriter<T> {
    private final String sql;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SqlParameterSourceConverter<T> parameterSourceConverter;

    public NamedParameterJdbcWriter(DataSource dataSource, String sql,
                                    SqlParameterSourceConverter<T> parameterSourceConverter) {
        this.sql = sql;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.parameterSourceConverter = parameterSourceConverter;
    }

    public NamedParameterJdbcWriter(DataSource dataSource, String sql) {
        this(dataSource, sql, new BeanSqlParameterSourceConverter<>());
    }

    @Override
    public void begin() {

    }

    @Override
    public void write(List<DataContext<T>> dataContexts) {
        SqlParameterSource[] batchArgs = parameterSourceConverter.convert(dataContexts);
        namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public void finish() {

    }
}
