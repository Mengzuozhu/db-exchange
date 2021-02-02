package com.github.mzz.exchange.core.writer;

/**
 * The interface Default data writer.
 *
 * @param <T> the type parameter
 * @author mengzz
 * @since 2020 /12/30
 */
@FunctionalInterface
public interface DefaultDataWriter<T> extends DataWriter<T> {
    /**
     * Begin.
     */
    @Override
    default void begin() {

    }

    /**
     * Finish.
     */
    @Override
    default void finish() {

    }
}
