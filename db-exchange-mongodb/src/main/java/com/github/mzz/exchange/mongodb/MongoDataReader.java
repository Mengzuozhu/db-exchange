package com.github.mzz.exchange.mongodb;

import com.github.mzz.exchange.core.model.CommonDataContext;
import com.github.mzz.exchange.core.model.DataContext;
import com.github.mzz.exchange.core.reader.DataReader;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import lombok.RequiredArgsConstructor;

/**
 * The type Mongo reader.
 *
 * @param <T> the type parameter
 * @author mengzz
 */
@RequiredArgsConstructor
public class MongoDataReader<T> implements DataReader<T> {
    private final MongoQuery<T> mongoQuery;
    private MongoCursor<T> cursor;

    /**
     * Begin.
     */
    @Override
    public void begin() {
        FindIterable<T> findIterable = mongoQuery.getCollection()
                .find(mongoQuery.getQuery());
        findIterable.noCursorTimeout(true);
        int limit = mongoQuery.getLimit();
        int skip = mongoQuery.getSkip();
        if (limit > 0) {
            findIterable.limit(limit);
        }
        if (skip > 0) {
            findIterable.skip(skip);
        }
        cursor = findIterable.iterator();
    }

    /**
     * Read.
     *
     * @return the t
     */
    @Override
    public DataContext<T> read() {
        return cursor.hasNext() ? CommonDataContext.of(cursor.next()) : null;
    }

    @Override
    public int getBatchSize() {
        return mongoQuery.getBatchSize();
    }

    /**
     * Finish.
     */
    @Override
    public void finish() {
        cursor.close();
    }
}
