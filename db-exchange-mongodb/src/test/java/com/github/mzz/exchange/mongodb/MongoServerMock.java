package com.github.mzz.exchange.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import lombok.Getter;
import org.bson.Document;

import java.net.InetSocketAddress;

/**
 * @author mengzz
 **/
public class MongoServerMock {
    @Getter
    private MongoCollection<Document> collection;
    private MongoClient client;
    private MongoServer server;

    public MongoServerMock() {
        server = new MongoServer(new MemoryBackend());
        InetSocketAddress serverAddress = server.bind();
        client = new MongoClient(new ServerAddress(serverAddress));
        collection = client.getDatabase(MongoTestUtil.DATABASE_NAME).getCollection(MongoTestUtil.COLLECTION_NAME);
    }

    public void shutdown() {
        client.close();
        server.shutdown();
    }

}
