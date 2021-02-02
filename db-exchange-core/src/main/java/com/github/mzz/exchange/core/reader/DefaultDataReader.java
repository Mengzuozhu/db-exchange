package com.github.mzz.exchange.core.reader;

/**
 * The interface Default data reader.
 *
 * @param <T> the type parameter
 * @author mengzz
 * @since 2020 /12/30
 */
public interface DefaultDataReader<T> extends DataReader<T> {
    /**
     * Begin.
     */
    @Override
    default void begin() {

    }

    /**
     * Gets batch size.
     *
     * @return the batch size
     */
    @Override
    default int getBatchSize() {
        return Integer.MAX_VALUE;
    }

    /**
     * Finish.
     */
    @Override
    default void finish() {

    }
}
