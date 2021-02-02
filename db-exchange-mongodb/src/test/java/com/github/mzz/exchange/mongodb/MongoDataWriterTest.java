package com.github.mzz.exchange.mongodb;

import com.github.mzz.exchange.core.exchange.ExchangeTemplate;
import com.github.mzz.exchange.core.reader.IterableReader;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author mengzz
 **/
class MongoDataWriterTest {
    private static MongoServerMock mongoServerMock = new MongoServerMock();
    private static MongoCollection<Document> collection = mongoServerMock.getCollection();

    @AfterAll
    static void afterAll() {
        mongoServerMock.shutdown();
    }

    @Test
    void write() {
        Document document = MongoTestUtil.getDocument();
        IterableReader<Document> reader = IterableReader.of(Lists.newArrayList(document));
        MongoDataWriter<Document> mongoDataWriter = new MongoDataWriter<>(collection);
        ExchangeTemplate.from(reader)
                .execute(mongoDataWriter)
                .await();
        Document first = collection.find(document).first();
        Assertions.assertNotNull(first);
        Assertions.assertEquals(MongoTestUtil.DOC_ID, first.get(MongoTestUtil.ID));
    }
}
