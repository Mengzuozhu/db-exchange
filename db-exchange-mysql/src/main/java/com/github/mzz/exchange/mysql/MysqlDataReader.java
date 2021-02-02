package com.github.mzz.exchange.mysql;

import com.github.mzz.exchange.mysql.mapper.ResultSetMapper;
import com.github.mzz.exchange.core.model.CommonDataContext;
import com.github.mzz.exchange.core.model.DataContext;
import com.github.mzz.exchange.core.reader.DataReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author mengzz
 **/
@Slf4j
public class MysqlDataReader<T> implements DataReader<T> {
    private final MysqlQuery mysqlQuery;
    private DataSource dataSource;
    private String sql;
    private Statement statement;
    private Connection connection;
    private ResultSet resultSet;
    @SuppressWarnings("unchecked")
    private ResultSetMapper<T> resultSetMapper = rs -> (T) rs;

    public MysqlDataReader(MysqlQuery mysqlQuery) {
        this(mysqlQuery, null);
    }

    public MysqlDataReader(MysqlQuery mysqlQuery, ResultSetMapper<T> resultSetMapper) {
        Validate.isTrue(mysqlQuery != null, "the parameter mysqlQuery require not null");
        this.mysqlQuery = mysqlQuery;
        this.dataSource = mysqlQuery.getDataSource();
        this.sql = mysqlQuery.getSql();
        this.statement = mysqlQuery.getStatement();
        if (resultSetMapper != null) {
            this.resultSetMapper = resultSetMapper;
        }
    }

    @SneakyThrows
    @Override
    public void begin() {
        if (statement == null) {
            connection = dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            setFetchSize();
        }
        resultSet = statement.executeQuery(sql);
    }

    @Override
    public DataContext<T> read() {
        try {
            if (resultSet.next()) {
                T data = resultSetMapper.map(resultSet);
                return CommonDataContext.of(data);
            }
        } catch (SQLException e) {
            log.error("read fail", e);
            ExceptionUtils.rethrow(e);
        }
        return null;
    }

    @Override
    public int getBatchSize() {
        return mysqlQuery.getBatchSize();
    }

    @SneakyThrows
    @Override
    public void finish() {
        if (mysqlQuery.isAutoClose()) {
            DbUtils.close(connection);
            DbUtils.close(statement);
            DbUtils.close(resultSet);
        }
    }

    private void setFetchSize() throws SQLException {
        int batchSize = getBatchSize();
        if (batchSize != Integer.MAX_VALUE) {
            statement.setFetchSize(batchSize);
        }
    }

}
