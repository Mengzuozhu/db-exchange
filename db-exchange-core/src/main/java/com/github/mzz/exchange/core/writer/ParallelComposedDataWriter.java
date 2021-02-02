package com.github.mzz.exchange.core.writer;

import com.github.mzz.exchange.core.model.DataContext;

import java.util.List;

/**
 * The type Parallel composed data writer.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
public class ParallelComposedDataWriter<T> extends ComposedDataWriter<T> {

    /**
     * Write.
     *
     * @param dataContexts the data contexts
     */
    @Override
    public void write(List<DataContext<T>> dataContexts) {
        writers.parallelStream()
                .forEach(data -> data.write(dataContexts));
    }
}
