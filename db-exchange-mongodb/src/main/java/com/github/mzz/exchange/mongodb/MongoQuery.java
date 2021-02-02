package com.github.mzz.exchange.mongodb;

import com.mongodb.client.MongoCollection;
import lombok.*;
import org.bson.conversions.Bson;

/**
 * The type Mongo query.
 *
 * @param <T> the type parameter
 * @author mengzz
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MongoQuery<T> {
    @NonNull
    private MongoCollection<T> collection;
    @NonNull
    private Bson query;
    @NonNull
    private Integer batchSize;
    private int limit;
    private int skip;
}
