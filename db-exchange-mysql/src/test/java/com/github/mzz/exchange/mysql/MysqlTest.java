package com.github.mzz.exchange.mysql;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * @author mengzz
 **/
@Slf4j
public class MysqlTest {
    static final String TABLE = "user";
    static final String QUERY_SQL = String.format("SELECT * FROM %s LIMIT 1", TABLE);
    static final String NAME = "name";
    static final String NAME_VALUE = "test";
    static String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false;MODE=MYSQL;" +
            "INIT=runscript from 'classpath:schema.sql'";
    static String h2Driver = "org.h2.Driver";
    static DataSource dataSource = MysqlUtil.getDataSource(h2Driver, url, "test", "654321");

    static DataSource getH2DataSource() {
        return dataSource;
    }
}
