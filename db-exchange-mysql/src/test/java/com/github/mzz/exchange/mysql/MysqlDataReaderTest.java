package com.github.mzz.exchange.mysql;

import com.github.mzz.exchange.core.exchange.ExchangeTemplate;
import com.github.mzz.exchange.core.writer.ListDataCollector;
import com.github.mzz.exchange.mysql.mapper.ResultSetMapper;
import com.github.mzz.exchange.mysql.mapper.ResultSetToBean;
import com.github.mzz.exchange.mysql.mapper.ResultSetToMap;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author mengzz
 **/
class MysqlDataReaderTest {
    private static DataSource dataSource = MysqlTest.getH2DataSource();

    @AfterAll
    static void afterAll() throws SQLException {
        MysqlUtil.truncateTable(dataSource, MysqlTest.TABLE);
    }

    @BeforeAll
    static void beforeAll() throws SQLException {
        MysqlUtil.insert(dataSource, String.format("insert into %s (name) values ('test')", MysqlTest.TABLE),
                new MapHandler());
    }

    @Test
    void readMap() {
        ResultSetMapper<Map<String, Object>> mapper = new ResultSetToMap();
        MysqlDataReader<Map<String, Object>> mysqlDataReader = new MysqlDataReader<>(getMysqlQuery(), mapper);
        execute(mysqlDataReader);
    }

    @Test
    void readBean() {
        ResultSetMapper<User> mapper = new ResultSetToBean<>(User.class);
        MysqlDataReader<User> mysqlDataReader = new MysqlDataReader<>(getMysqlQuery(), mapper);
        execute(mysqlDataReader);
    }

    private <T> void execute(MysqlDataReader<T> mysqlDataReader) {
        ListDataCollector<T> listDataCollector = new ListDataCollector<>();
        ExchangeTemplate.from(mysqlDataReader)
                .execute(listDataCollector)
                .await();
        Assertions.assertEquals(1, listDataCollector.getResult().size());
    }

    private MysqlQuery getMysqlQuery() {
        return MysqlQuery.builder()
                .dataSource(dataSource)
                .sql(MysqlTest.QUERY_SQL)
                .batchSize(10)
                .build();
    }

}
