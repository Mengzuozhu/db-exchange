package com.github.mzz.exchange.mongodb;

import com.github.mzz.exchange.core.writer.DirectDataWriter;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author mengzz
 **/
@RequiredArgsConstructor
public class MongoDataWriter<T> implements DirectDataWriter<T> {
    private final MongoCollection<T> collection;

    @Override
    public void writeDirectly(List<T> data) {
        collection.insertMany(data);
    }
}
