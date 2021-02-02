package com.github.mzz.exchange.core.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author mengzz
 * @date 2020/9/22
 **/
@RequiredArgsConstructor(staticName = "of")
@Data
public class CommonDataContext<T> implements DataContext<T> {
    private final T data;
}
