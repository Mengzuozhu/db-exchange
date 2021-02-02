package com.github.mzz.exchange.mongodb;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.internal.connection.ServerAddressHelper.createServerAddress;

/**
 * The type Mongo util.
 *
 * @author mengzz
 */
public class MongoUtil {

    /**
     * Gets client.
     *
     * @param host the host
     * @return the client
     */
    public static MongoClient getClient(String host) {
        return new MongoClient(createServerAddress(host));
    }

    /**
     * Gets client.
     *
     * @param host the host
     * @param port the port
     * @return the client
     */
    public static MongoClient getClient(String host, int port) {
        return new MongoClient(createServerAddress(host, port));
    }

    /**
     * Gets client.
     *
     * @param host     the host
     * @param port     the port
     * @param username the username
     * @param password the password
     * @param dbName   the db name
     * @return the client
     */
    public static MongoClient getClient(String host, int port, String username, String password, String dbName) {
        ServerAddress serverAddress = new ServerAddress(host, port);
        MongoCredential credential = MongoCredential.createScramSha1Credential(username, dbName,
                password.toCharArray());
        return new MongoClient(Lists.newArrayList(serverAddress), credential, new MongoClientOptions.Builder().build());
    }

    /**
     * Foreach all documents.
     *
     * @param collection the collection
     * @param consumer   the consumer
     * @return the int
     */
    public static int foreachAllDocuments(MongoCollection<Document> collection, Consumer<Document> consumer) {
        return foreachDocuments(collection, new BsonDocument(), 0, 0, consumer);
    }

    /**
     * Foreach documents.
     *
     * @param collection the collection
     * @param query      the query
     * @param consumer   the consumer
     * @return the int
     */
    public static int foreachDocuments(MongoCollection<Document> collection, Bson query, Consumer<Document> consumer) {
        return foreachDocuments(collection, query, 0, 0, consumer);
    }

    /**
     * Foreach documents.
     *
     * @param collection the collection
     * @param query      the query
     * @param limit      the limit
     * @param skip       the skip
     * @param consumer   the consumer
     * @return the int
     */
    public static int foreachDocuments(MongoCollection<Document> collection, Bson query, int limit, int skip,
                                       Consumer<Document> consumer) {
        if (collection == null) {
            return 0;
        }
        FindIterable<Document> documents = collection.find(query);
        documents.noCursorTimeout(true)
                .limit(limit)
                .skip(skip);
        return consumeDocument(documents, consumer);
    }

    /**
     * Consume document int.
     *
     * @param documents the documents
     * @param consumer  the consumer
     * @return the int
     */
    public static int consumeDocument(FindIterable<Document> documents, Consumer<Document> consumer) {
        int count = 0;
        for (Document document : documents) {
            consumer.accept(document);
            count++;
        }
        return count;
    }

    /**
     * List collection names list.
     *
     * @param host   the host
     * @param dbName the db name
     * @return the list
     */
    public static List<String> listCollectionNames(String host, String dbName) {
        return listCollectionNames(new MongoClient(host), dbName);
    }

    /**
     * List collection names list.
     *
     * @param dbName      the db name
     * @param mongoClient the mongo client
     * @return the list
     */
    public static List<String> listCollectionNames(MongoClient mongoClient, String dbName) {
        MongoIterable<String> collectionNames = mongoClient.getDatabase(dbName).listCollectionNames();
        return collectionNames.into(new ArrayList<>());
    }

}
