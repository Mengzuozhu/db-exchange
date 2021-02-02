package com.github.mzz.exchange.core.writer;

import com.github.mzz.exchange.core.model.DataContext;

import java.util.List;

/**
 * The interface Data writer.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
public interface DataWriter<T> {
    /**
     * Begin.
     */
    void begin();

    /**
     * Write.
     *
     * @param dataContexts the dataContexts
     */
    void write(List<DataContext<T>> dataContexts);

    /**
     * Finish.
     */
    void finish();
}
