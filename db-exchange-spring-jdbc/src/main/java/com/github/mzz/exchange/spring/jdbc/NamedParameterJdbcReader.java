package com.github.mzz.exchange.spring.jdbc;

import com.github.mzz.exchange.core.model.CommonDataContext;
import com.github.mzz.exchange.core.model.DataContext;
import com.github.mzz.exchange.core.reader.DataReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * The type Named parameter jdbc reader.
 *
 * @author mengzz
 * @see NamedParameterJdbcTemplate
 */
@Slf4j
public class NamedParameterJdbcReader implements DataReader<ResultSet> {
    private final DataSource dataSource;
    private final String sql;
    private SqlParameterSource paramSource;
    private ResultSet resultSet;
    private PreparedStatement ps;
    private Connection con;
    private int batchSize = Integer.MAX_VALUE;

    public NamedParameterJdbcReader(DataSource dataSource, String sql, SqlParameterSource paramSource) {
        this.dataSource = dataSource;
        this.sql = sql;
        this.paramSource = paramSource;
    }

    public NamedParameterJdbcReader(DataSource dataSource, String sql, Map<String, ?> paramMap) {
        this(dataSource, sql, new MapSqlParameterSource(paramMap));
    }

    public NamedParameterJdbcReader(DataSource dataSource, String sql) {
        this(dataSource, sql, EmptySqlParameterSource.INSTANCE);
    }

    public NamedParameterJdbcReader batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @Override
    public void begin() {
        PreparedStatementCreator psc = getPreparedStatementCreator(sql, paramSource);
        con = DataSourceUtils.getConnection(dataSource);
        try {
            ps = psc.createPreparedStatement(con);
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            log.error("begin fail", e);
            close();
        }
    }

    @Override
    public DataContext<ResultSet> read() {
        try {
            if (resultSet.next()) {
                return CommonDataContext.of(resultSet);
            }
        } catch (SQLException e) {
            log.error("read fail", e);
            ExceptionUtils.rethrow(e);
        }
        return null;
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public void finish() {
        close();
    }

    private PreparedStatementCreator getPreparedStatementCreator(String sql, SqlParameterSource paramSource) {
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
        return factory.newPreparedStatementCreator(params);
    }

    private void close() {
        JdbcUtils.closeStatement(ps);
        DataSourceUtils.releaseConnection(con, dataSource);
    }
}
