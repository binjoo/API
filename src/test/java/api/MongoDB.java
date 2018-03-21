package api;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("apidb");
        MongoCollection<Document> mc = mongoDatabase.getCollection("users");
        Document doc = new Document();
        doc.append("appkey", "858db5fa2a99450185b806998bcc8155");
        doc.append("douban_module", true);
        doc.append("douban_id", "asdasd");
        doc.append("music_module", false);
        doc.append("xingzhe_module", false);
        mc.insertOne(doc);
    }

    public static void main1(String[] args) {
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");
            // mongoDatabase.createCollection("test");

            MongoCollection<Document> mc = mongoDatabase.getCollection("test");

            Document doc = new Document();
            doc.append("douban", 1);
            doc.append("music", 0);

            List<Document> list = new ArrayList<Document>();
            list.add(doc);

            // mc.insertMany(list);

            Document my = new Document();
            my.append("key", "ce61bc610f2148f88c13182ae4768e37");
            my.append("douban", 1);
            my.append("music", 1);

            // mc.insertOne(my);

            Document cha = new Document();
            cha.append("key", "ce61bc610f2148f88c13182ae4768e37");

            FindIterable<Document> iter = mc.find(cha);

            iter.forEach(new Block<Document>() {
                public void apply(Document arg0) {
                    System.err.println(arg0.toJson());
                }
            });
            Document xxx = mc.find(cha).first();
            System.out.println(xxx.toJson());

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
