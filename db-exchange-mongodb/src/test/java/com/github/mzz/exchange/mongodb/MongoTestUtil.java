package com.github.mzz.exchange.mongodb;

import com.google.common.collect.ImmutableMap;
import org.bson.Document;

/**
 * @author mengzz
 **/
public class MongoTestUtil {
    static final String DATABASE_NAME = "test";
    static final String COLLECTION_NAME = "test_db";
    static final String ID = "id";
    static final int DOC_ID = 1;

    static Document getDocument() {
        return new Document(ImmutableMap.of(ID, DOC_ID));
    }
}
