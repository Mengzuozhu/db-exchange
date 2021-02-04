package com.github.mzz.exchange.mysql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * @author mengzz
 **/
@Slf4j
public class MysqlTest {
    static final String CJ_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String TABLE = "user";
    static final String QUERY_SQL = String.format("SELECT * FROM %s LIMIT 1", TABLE);
    static final String NAME = "name";
    static final String NAME_VALUE = "test";
    static String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false;MODE=MYSQL;" +
            "INIT=runscript from 'classpath:schema.sql'";
    static String h2Driver = "org.h2.Driver";
    static DataSource dataSource = getDataSource(h2Driver, url, "test", "654321");

    public static DataSource getDataSource(String url, String username, String password) {
        return getDataSource(CJ_JDBC_DRIVER, url, username, password);
    }

    public static DataSource getDataSource(String driverClassName, String url, String username, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    static DataSource getH2DataSource() {
        return dataSource;
    }

}
