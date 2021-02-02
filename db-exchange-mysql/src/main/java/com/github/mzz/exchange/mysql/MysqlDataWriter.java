package com.github.mzz.exchange.mysql;

import com.github.mzz.exchange.core.model.DataContext;
import com.github.mzz.exchange.core.writer.DataWriter;
import com.github.mzz.exchange.mysql.setter.PreparedStatementSetter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author mengzz
 **/
@Slf4j
public class MysqlDataWriter<T> implements DataWriter<T> {
    private final DataSource dataSource;
    private final String sql;
    private final PreparedStatementSetter<T> preparedStatementSetter;
    private Connection connection;

    public MysqlDataWriter(DataSource dataSource, String sql, PreparedStatementSetter<T> preparedStatementSetter) {
        this.dataSource = dataSource;
        this.sql = sql;
        this.preparedStatementSetter = preparedStatementSetter;
    }

    @SneakyThrows
    @Override
    public void begin() {
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
    }

    @SneakyThrows
    @Override
    public void write(List<DataContext<T>> dataContexts) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (DataContext<T> dataContext : dataContexts) {
                preparedStatementSetter.fillStatement(preparedStatement, dataContext.getData());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            commit();
        }
    }

    @SneakyThrows
    @Override
    public void finish() {
        DbUtils.close(connection);
    }

    private void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            log.error("fail to commit", e);
            rollback();
            log.debug("rolled back finish");
            ExceptionUtils.rethrow(e);
        }
    }

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error("rolled back fail", e);
            ExceptionUtils.rethrow(e);
        }
    }
}
