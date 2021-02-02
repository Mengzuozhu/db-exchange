package com.github.mzz.exchange.core.exchange;

import com.github.mzz.exchange.core.ExchangeTestUtil;
import com.github.mzz.exchange.core.reader.DefaultDataReader;
import com.github.mzz.exchange.core.model.CommonDataContext;
import com.github.mzz.exchange.core.model.DataContext;
import com.github.mzz.exchange.core.writer.ListDataCollector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author mengzz
 * @since 2020/12/30
 **/
public class ExchangeTemplateTest {

    @Test
    public void map() {
        String prefix = "test_";
        ListDataCollector<String> listDataCollector = new ListDataCollector<>();
        ExchangeTemplate.from(ExchangeTestUtil.getStringDataReader())
                .map(context -> CommonDataContext.of(prefix + context.getData()))
                .execute(listDataCollector)
                .await();
        List<String> result = listDataCollector.getResult();
        Assertions.assertEquals(ExchangeTestUtil.limit, result.size());
        Assertions.assertTrue(result.stream().allMatch(s -> s.startsWith(prefix)));
    }

    @Test
    public void filter() {
        String expected = "1";
        Predicate<DataContext<String>> filter = data -> expected.equals(data.getData());
        ListDataCollector<String> listDataCollector = new ListDataCollector<>();
        ExchangeTemplate.from(ExchangeTestUtil.getStringDataReader())
                .filter(filter)
                .execute(listDataCollector)
                .await();
        Assertions.assertTrue(listDataCollector.getResult().stream()
                .allMatch(expected::equals));
    }

    @Test
    public void scheduler() {
        ListDataCollector<String> listDataCollector = new ListDataCollector<>();
        ExchangeTemplate.from(ExchangeTestUtil.getStringDataReader())
                .scheduler(Schedulers.immediate())
                .execute(listDataCollector);
        Assertions.assertEquals(ExchangeTestUtil.limit, listDataCollector.getResult().size());
    }

    @Test
    public void execute_should_throw_exception_when_error() {
        Assertions.assertThrows(IllegalStateException.class, () ->
                ExchangeTemplate.from((DefaultDataReader<String>) () -> {
                    throw new IllegalStateException();
                })
                        .scheduler(Schedulers.immediate())
                        .execute(new ListDataCollector<>()));
    }

}
