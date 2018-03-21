package com.binjoo.base.db;

import org.bson.Document;

import com.binjoo.base.utils.AppConfig;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtils {
    private static MongoClient client = null;
    private static MongoDatabase database = null;

    static {
        client = new MongoClient(AppConfig.getPro("db_host"), AppConfig.getIntPro("db_port"));
        database = client.getDatabase("apidb");
    }

    public static Document getItemOne(String table, Document doc) throws Exception {
        return database.getCollection(table).find(doc).first();
    }

    public static FindIterable<Document> getItemMany(String table, Document doc) throws Exception {
        return database.getCollection(table).find(doc);
    }
}
