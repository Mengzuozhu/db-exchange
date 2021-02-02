package com.github.mzz.exchange.spring.jdbc;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.github.mzz.exchange.core.exchange.ExchangeTemplate;
import com.github.mzz.exchange.core.reader.IterableReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mengzz
 **/
class NamedParameterJdbcWriterTest extends EmbeddedDatabaseTest {
    private static final long ID1 = 1L;
    private static final long ID2 = 2L;
    private static final String INSERT_SQL = MessageFormat.format("INSERT INTO {0} (id, name) VALUES (:id, :name)",
            MysqlTest.TABLE);
    private static final String QUERY_SQL = String.format("SELECT * FROM %s", MysqlTest.TABLE);

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void writeFromBean() {
        List<User> users = getUsers();
        IterableReader<User> reader = IterableReader.of(users);
        NamedParameterJdbcWriter<User> jdbcWriter = new NamedParameterJdbcWriter<>(embeddedDatabase,
                INSERT_SQL);
        ExchangeTemplate.from(reader)
                .execute(jdbcWriter)
                .await();
        List<User> result = namedParameterJdbcTemplate.query(QUERY_SQL, BeanPropertyRowMapper.newInstance(User.class));
        assertEquals(users, result);
    }

    @Test
    void writeFromMap() {
        Map<String, Object> map = ImmutableMap.of("id", ID1,
                MysqlTest.NAME, MysqlTest.NAME_VALUE);
        List<Map<String, Object>> maps = Lists.newArrayList(map);
        IterableReader<Map<String, Object>> reader = IterableReader.of(maps);
        NamedParameterJdbcWriter<Map<String, Object>> jdbcWriter = new NamedParameterJdbcWriter<>(embeddedDatabase,
                INSERT_SQL, new MapSqlParameterSourceConverter<>(t -> t));
        ExchangeTemplate.from(reader)
                .execute(jdbcWriter)
                .await();

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query(QUERY_SQL, new ColumnMapRowMapper());
        LinkedCaseInsensitiveMap<Object> insensitiveMap = new LinkedCaseInsensitiveMap<>();
        insensitiveMap.putAll(map);
        assertEquals(Lists.newArrayList(insensitiveMap), result);
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

}
