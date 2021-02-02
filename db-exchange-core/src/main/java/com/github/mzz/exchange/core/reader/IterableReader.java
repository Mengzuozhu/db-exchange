package com.github.mzz.exchange.core.reader;

import com.github.mzz.exchange.core.model.CommonDataContext;
import com.github.mzz.exchange.core.model.DataContext;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author mengzz
 **/
@RequiredArgsConstructor
public class IterableReader<T> implements DataReader<T> {
    private final Iterable<? extends T> iterable;
    private final int batchSize;
    private Iterator<? extends T> iterator;

    public static <T> IterableReader<T> of(Iterable<T> iterable, int batchSize) {
        return new IterableReader<>(iterable, batchSize);
    }

    public static <T> IterableReader<T> of(Collection<T> collection) {
        return of(collection, collection.size());
    }

    public static <T> IterableReader<T> of(Collection<T> collection, int batchSize) {
        return new IterableReader<>(collection, batchSize);
    }

    @Override
    public void begin() {
        iterator = iterable.iterator();
    }

    @Override
    public DataContext<T> read() {
        return iterator.hasNext() ? CommonDataContext.of(iterator.next()) : null;
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public void finish() {
        iterator = null;
    }
}
