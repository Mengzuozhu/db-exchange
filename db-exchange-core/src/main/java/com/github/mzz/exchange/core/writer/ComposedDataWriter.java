package com.github.mzz.exchange.core.writer;

import com.google.common.collect.Lists;
import com.github.mzz.exchange.core.model.DataContext;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Composed data writer.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
@RequiredArgsConstructor
public class ComposedDataWriter<T> implements DataWriter<T> {
    protected final List<DataWriter<T>> writers;

    public ComposedDataWriter() {
        this(new ArrayList<>());
    }

    @SafeVarargs
    public ComposedDataWriter(DataWriter<T>... writers) {
        this(Lists.newArrayList(writers));
    }

    /**
     * Of.
     *
     * @param <T>     the type parameter
     * @param writers the writers
     * @return the composed data writer
     */
    @SafeVarargs
    public static <T> ComposedDataWriter<T> of(DataWriter<T>... writers) {
        return new ComposedDataWriter<>(writers);
    }

    /**
     * Of.
     *
     * @param <T>     the type parameter
     * @param writers the writers
     * @return the composed data writer
     */
    public static <T> ComposedDataWriter<T> of(List<DataWriter<T>> writers) {
        return new ComposedDataWriter<>(writers);
    }

    /**
     * Begin.
     */
    @Override
    public void begin() {
        writers.forEach(DataWriter::begin);
    }

    /**
     * Write.
     *
     * @param dataContexts the data contexts
     */
    @Override
    public void write(List<DataContext<T>> dataContexts) {
        writers.forEach(data -> data.write(dataContexts));
    }

    /**
     * Finish.
     */
    @Override
    public void finish() {
        writers.forEach(DataWriter::finish);
    }
}
