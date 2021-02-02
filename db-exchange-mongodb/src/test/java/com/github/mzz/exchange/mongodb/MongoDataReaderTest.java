package com.github.mzz.exchange.mongodb;

import com.mongodb.client.MongoCollection;
import com.github.mzz.exchange.core.exchange.ExchangeTemplate;
import com.github.mzz.exchange.core.writer.ListDataCollector;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author mengzz
 **/
class MongoDataReaderTest {
    private static MongoServerMock mongoServerMock = new MongoServerMock();
    private static MongoCollection<Document> collection = mongoServerMock.getCollection();

    @BeforeAll
    static void beforeAll() {
        collection.insertOne(MongoTestUtil.getDocument());
    }

    @AfterAll
    static void afterAll() {
        mongoServerMock.shutdown();
    }

    @Test
    void read() {
        MongoQuery<Document> mongoQuery = MongoQuery.<Document>builder()
                .collection(collection)
                .query(MongoTestUtil.getDocument())
                .batchSize(10)
                .build();

        MongoDataReader<Document> mongoDataReader = new MongoDataReader<>(mongoQuery);
        ListDataCollector<Document> listDataCollector = new ListDataCollector<>();
        ExchangeTemplate.from(mongoDataReader)
                .execute(listDataCollector)
                .await();
        List<Document> result = listDataCollector.getResult();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(MongoTestUtil.DOC_ID, result.get(0).get(MongoTestUtil.ID));
    }

}
