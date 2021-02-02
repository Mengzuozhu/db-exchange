package com.github.mzz.exchange.mysql;

import com.github.mzz.exchange.core.exchange.ExchangeTemplate;
import com.github.mzz.exchange.core.reader.IterableReader;
import com.github.mzz.exchange.mysql.setter.ArrayPreparedStatementSetter;
import com.github.mzz.exchange.mysql.setter.BeanPreparedStatementSetter;
import com.github.mzz.exchange.mysql.setter.ListPreparedStatementSetter;
import com.github.mzz.exchange.mysql.setter.MapPreparedStatementSetter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author mengzz
 **/
class MysqlDataWriterTest {
    private static final long ID1 = 1L;
    private static final long ID2 = 2L;
    private static final String INSERT_SQL = String.format("INSERT INTO %s (id, %s) VALUES (?, ?)", MysqlTest.TABLE,
            MysqlTest.NAME);
    private static final String[] PROPERTIES = new String[]{"id", MysqlTest.NAME};
    private static DataSource dataSource = MysqlTest.getH2DataSource();

    @AfterEach
    void tearDown() throws SQLException {
        MysqlUtil.truncateTable(dataSource, MysqlTest.TABLE);
    }

    @Test
    void writeArray() throws SQLException {
        Object[] elements = {ID1, MysqlTest.NAME_VALUE};
        Object[] elements2 = {ID2, MysqlTest.NAME_VALUE};
        List<Object[]> sources = Lists.newArrayList(elements, elements2);
        IterableReader<Object[]> reader = IterableReader.of(sources);
        MysqlDataWriter<Object[]> mysqlDataWriter = new MysqlDataWriter<>(dataSource, INSERT_SQL,
                new ArrayPreparedStatementSetter());
        execute(sources, reader, mysqlDataWriter);
    }

    @Test
    void writeList() throws SQLException {
        List<List<Object>> sources = Lists.newArrayList(Lists.newArrayList(ID1, MysqlTest.NAME_VALUE),
                Lists.newArrayList(ID2, MysqlTest.NAME_VALUE));
        IterableReader<List<Object>> reader = IterableReader.of(sources);
        MysqlDataWriter<List<Object>> mysqlDataWriter = new MysqlDataWriter<>(dataSource, INSERT_SQL,
                new ListPreparedStatementSetter<>());
        execute(sources, reader, mysqlDataWriter);
    }

    @Test
    void writeMap() throws SQLException {
        ImmutableMap<Integer, Object> indexAndVal = ImmutableMap.of(1, ID1, 2, MysqlTest.NAME_VALUE);
        ImmutableMap<Integer, Object> indexAndVal2 = ImmutableMap.of(1, ID2, 2, MysqlTest.NAME_VALUE);
        List<Map<Integer, Object>> sources = Lists.newArrayList(indexAndVal, indexAndVal2);
        IterableReader<Map<Integer, Object>> reader = IterableReader.of(sources);
        MysqlDataWriter<Map<Integer, Object>> mysqlDataWriter = new MysqlDataWriter<>(dataSource, INSERT_SQL,
                new MapPreparedStatementSetter<>());
        execute(sources, reader, mysqlDataWriter);
    }

    @Test
    void writeBean() throws SQLException {
        List<User> users = getUsers();
        IterableReader<User> reader = IterableReader.of(users);
        BeanPreparedStatementSetter<User> statementSetter = new BeanPreparedStatementSetter<>(User.class);
        MysqlDataWriter<User> mysqlDataWriter = new MysqlDataWriter<>(dataSource, INSERT_SQL, statementSetter);
        execute(users, reader, mysqlDataWriter);
    }

    @Test
    void writeBeanWithProperties() throws SQLException {
        List<User> users = getUsers();
        IterableReader<User> reader = IterableReader.of(users);
        BeanPreparedStatementSetter<User> statementSetter = new BeanPreparedStatementSetter<>(User.class, PROPERTIES);
        MysqlDataWriter<User> mysqlDataWriter = new MysqlDataWriter<>(dataSource, INSERT_SQL, statementSetter);
        execute(users, reader, mysqlDataWriter);
    }

    private List<User> getUsers() {
        User user = User.builder()
                .id(ID1)
                .name(MysqlTest.NAME_VALUE)
                .build();
        User user2 = User.builder()
                .id(ID2)
                .name(MysqlTest.NAME_VALUE)
                .build();
        return Lists.newArrayList(user, user2);
    }

    private <T> void execute(List<T> sources, IterableReader<T> reader, MysqlDataWriter<T> mysqlDataWriter) throws SQLException {
        ExchangeTemplate.from(reader)
                .execute(mysqlDataWriter)
                .await();
        String query = String.format("SELECT * FROM %s where %s = '%s'", MysqlTest.TABLE, MysqlTest.NAME,
                MysqlTest.NAME_VALUE);
        List<Map<String, Object>> result = MysqlUtil.query(dataSource, query, new MapListHandler());
        Assertions.assertEquals(sources.size(), result.size());
        boolean allMatch = result.stream()
                .map(map -> map.get(MysqlTest.NAME))
                .allMatch(data -> Objects.equals(MysqlTest.NAME_VALUE, data));
        Assertions.assertTrue(allMatch);
    }
}
