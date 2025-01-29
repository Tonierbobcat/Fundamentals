package com.loficostudios.fundamentals.utils.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBUtils {

    private static boolean inited;

    private static MongoClient client;

    private static MongoDatabase database;

    public static void initialize(MongoDBConfiguration conf) {
        if (inited)
            return;

        ConnectionString connectionString = conf.getConnectionString();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        client = MongoClients.create(settings);
        MongoDBUtils.database = client.getDatabase(conf.database());

//        MongoDBUtils.serverCollection = MongoDBUtils.database.getCollection(MarketplacePlugin.SERVER_COLLECTION);
        //        lgr.log(Level.INFO, "Is Connected: " + connected);
        inited = isConnected();
    }

    public static MongoCollection<Document> getCollection(String collection) {
        return database.getCollection(collection);
    }

    public static boolean isInited() {
        return inited;
    }

    public static MongoClient getClient() {
        return client;
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

//    @Deprecated(forRemoval = true)
    private static boolean isConnected() {
//        final MarketplacePlugin plugin = MarketplacePlugin.getInstance();
//        var lgr = plugin.getLogger();
        try {
            database.runCommand(new Document("ping", 1));
            return true;
        } catch (Exception e) {
//            lgr.log(Level.SEVERE, "Connection failed", e);
            return false;
        }
    }

}
