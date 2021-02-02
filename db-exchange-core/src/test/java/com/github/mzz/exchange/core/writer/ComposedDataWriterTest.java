package com.github.mzz.exchange.core.writer;

import com.github.mzz.exchange.core.ExchangeTestUtil;
import com.github.mzz.exchange.core.exchange.ExchangeTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author mengzz
 **/
class ComposedDataWriterTest {

    @Test
    void write() {
        ListDataCollector<String> listDataCollector = new ListDataCollector<>();
        ListDataCollector<String> listDataCollector2 = new ListDataCollector<>();
        DataWriter<String> dataWriter = ComposedDataWriter.of(listDataCollector, listDataCollector2);
        ExchangeTemplate.from(ExchangeTestUtil.getStringDataReader())
                .execute(dataWriter)
                .await();
        Assertions.assertEquals(ExchangeTestUtil.limit, listDataCollector.getResult().size());
        Assertions.assertEquals(ExchangeTestUtil.limit, listDataCollector2.getResult().size());
    }

    @Test
    void writeParallel() {
        ListDataCollector<String> listDataCollector = new ListDataCollector<>();
        ListDataCollector<String> listDataCollector2 = new ListDataCollector<>();
        DataWriter<String> dataWriter = ParallelComposedDataWriter.of(listDataCollector, listDataCollector2);
        ExchangeTemplate.from(ExchangeTestUtil.getStringDataReader())
                .execute(dataWriter)
                .await();
        Assertions.assertEquals(ExchangeTestUtil.limit, listDataCollector.getResult().size());
        Assertions.assertEquals(ExchangeTestUtil.limit, listDataCollector2.getResult().size());
    }
}
