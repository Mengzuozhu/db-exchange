package com.github.mzz.exchange.core.writer;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mengzz
 **/
public class ListDataCollector<T> implements DirectDataWriter<T> {
    @Getter
    private List<T> result = new ArrayList<>();

    @Override
    public void writeDirectly(List<T> data) {
        result.addAll(data);
    }
}
