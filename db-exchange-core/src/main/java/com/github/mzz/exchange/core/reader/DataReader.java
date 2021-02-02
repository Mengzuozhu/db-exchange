package com.github.mzz.exchange.core.reader;

import com.github.mzz.exchange.core.model.DataContext;

/**
 * The interface Data reader.
 *
 * @param <T> the type parameter
 * @author mengzz
 * @date 2020 /5/7
 */
public interface DataReader<T> {
    /**
     * Begin.
     */
    void begin();

    /**
     * Read.
     *
     * @return the t
     */
    DataContext<T> read();

    /**
     * Batch size.
     *
     * @return the int
     */
    int getBatchSize();

    /**
     * Finish.
     */
    void finish();
}
