package com.github.mzz.exchange.core.model;

/**
 * The interface Data context.
 *
 * @param <T> the type parameter
 * @author mengzz
 *
 */
public interface DataContext<T> {

    /**
     * Gets data.
     *
     * @return the data
     */
    default T getData() {
        return null;
    }

    /**
     * Is complete.
     *
     * @return the boolean
     */
    default boolean isComplete(){
        return getData() == null;
    }
}
