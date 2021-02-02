package com.github.mzz.exchange.core.processor;

import com.github.mzz.exchange.core.model.CommonDataContext;
import com.github.mzz.exchange.core.model.DataContext;

import java.util.function.Function;

/**
 * The type Default data mapper.
 *
 * @param <I> the type parameter
 * @param <O> the type parameter
 * @author mengzz
 */
public abstract class DefaultDataMapper<I, O> implements Function<DataContext<I>, DataContext<O>> {
    @Override
    public DataContext<O> apply(DataContext<I> dataContext) {
        return CommonDataContext.of(map(dataContext.getData()));
    }

    /**
     * Process.
     *
     * @param data the data
     * @return the o
     */
    protected abstract O map(I data);
}
