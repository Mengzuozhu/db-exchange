package com.github.mzz.exchange.spring.jdbc;

import com.github.mzz.exchange.core.exchange.ExchangeTemplate;
import com.github.mzz.exchange.core.writer.ListDataCollector;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengzz
 **/
class NamedParameterJdbcReaderTest extends EmbeddedDatabaseTest {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        namedParameterJdbcTemplate.update(String.format("insert into %s (name) values ('test')", MysqlTest.TABLE),
                new HashMap<>());
    }

    @Override
    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void read() {
        String sql = String.format("SELECT * FROM %s where name = :name", MysqlTest.TABLE);
        Map<String, String> paramMap = ImmutableMap.of(MysqlTest.NAME, MysqlTest.NAME_VALUE);
        NamedParameterJdbcReader jdbcReader = new NamedParameterJdbcReader(embeddedDatabase, sql, paramMap)
                .batchSize(10);
        ListDataCollector<User> listDataCollector = new ListDataCollector<>();
        ExchangeTemplate.from(jdbcReader)
                .map(ResultSetToBeanMapper.of(User.class))
                .execute(listDataCollector)
                .await();
        List<User> result = listDataCollector.getResult();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(MysqlTest.NAME_VALUE, result.get(0).getName());

    }
}
