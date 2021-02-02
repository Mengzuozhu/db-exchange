package com.github.mzz.exchange.core.writer;

import com.github.mzz.exchange.core.model.DataContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The interface Default data writer.
 *
 * @param <T> the type parameter
 * @author mengzz
 * @since 2020 /12/30
 */
@FunctionalInterface
public interface DirectDataWriter<T> extends DefaultDataWriter<T> {

    /**
     * Write.
     *
     * @param dataContexts the data contexts
     */
    @Override
    default void write(List<DataContext<T>> dataContexts) {
        List<T> list = dataContexts.stream()
                .map(DataContext::getData)
                .collect(Collectors.toList());
        writeDirectly(list);
    }

    /**
     * Write directly.
     *
     * @param data the data
     */
    void writeDirectly(List<T> data);

}
