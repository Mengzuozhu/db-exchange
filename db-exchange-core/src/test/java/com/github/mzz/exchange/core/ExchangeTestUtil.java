package com.github.mzz.exchange.core;

import com.github.mzz.exchange.core.reader.DataReader;
import com.github.mzz.exchange.core.reader.IterableReader;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author mengzz
 **/
public class ExchangeTestUtil {
    public static int limit = 20;
    public static int batchSize = 10;

    public static DataReader<String> getStringDataReader() {
        List<String> strings = IntStream.range(0, limit)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        return IterableReader.of(strings, batchSize);
    }
}
