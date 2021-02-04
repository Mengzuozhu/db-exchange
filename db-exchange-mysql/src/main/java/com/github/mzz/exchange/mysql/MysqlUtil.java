package com.github.mzz.exchange.mysql;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * The type Mysql util.
 *
 * @author mengzz
 */
public class MysqlUtil {

    public static <T> T query(DataSource dataSource, String sql, ResultSetHandler<T> rsh) throws SQLException {
        return new QueryRunner(dataSource).query(sql, rsh);
    }

    public static <T> T insert(DataSource dataSource, String sql, ResultSetHandler<T> rsh) throws SQLException {
        return new QueryRunner(dataSource).insert(sql, rsh);
    }

    public static int execute(DataSource dataSource, String sql) throws SQLException {
        return new QueryRunner(dataSource).execute(sql);
    }

    public static void truncateTable(DataSource dataSource, String table) throws SQLException {
        MysqlUtil.execute(dataSource, "truncate table " + table);
    }

    public static void foreachResultSet(ResultSet rs, Consumer<ResultSet> consumer) throws SQLException {
        while (rs.next()) {
            consumer.accept(rs);
        }
    }
}
