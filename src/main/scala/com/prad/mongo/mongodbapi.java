package com.prad.mongo;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClient;
import com.mongodb.BasicDBObject;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

public class mongodbapi {
    public static MongoClient mongo = new MongoClient( "localhost" , 27017 );
    public static MongoDatabase database;
    public static  MongoCollection<Document> collection;
    public static BasicDBObject searchQuery = new BasicDBObject();
    public static BasicDBObject updateQuery = new BasicDBObject();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    public static Integer totalcase ;
    public static Integer deathcase ;
    public static void main(String[] args) {
//        getoffsetrange(mongo,"corono","offset");
        offsetrangeupdate("partition0",1200,1300);
//        countrymetricupdate(mongo);

    }


    public static MongoCollection<Document> setdatabasecollection(String dbname,String collection) {
        return mongo.getDatabase(dbname).getCollection(collection);
    }

    public  static Document getoffsetrange(String partition,String dbname,String collection){
        MongoCollection<Document> mongocollection= setdatabasecollection(dbname,collection);
        System.out.println("Collection sampleCollection selected successfully");

        Document document = mongocollection.find(eq("sourcekey", partition)).first();
        if (document == null) {
            //Document does not exist
            System.out.println("No data");
        } else {
            //We found the document
            System.out.println(document);
        }
 return document;
    }

    public static void offsetrangeupdate(String partitiondet,Integer fromoffsetval,Integer tilloffsetval){
        MongoCollection<Document> mongocollection= setdatabasecollection("corono","offset");
        searchQuery.append("sourcekey", partitiondet);
        updateQuery.append("$set", new BasicDBObject().append("fromoffset", fromoffsetval).append("tilloffset",tilloffsetval));
        Document oldversiondoc = getoffsetrange(partitiondet,"corono","offset");
        UpdateResult updresult = mongocollection.updateMany(searchQuery, updateQuery);
        if(updresult.getModifiedCount() > 0)
        {
            System.out.println("DB updated");
        }else
        {
            Document base_doc = new Document().append("sourcekey",partitiondet).append("fromoffset", fromoffsetval).append("tilloffset",tilloffsetval);
            mongocollection.insertOne(base_doc);
            oldversiondoc.append("timestamp", new Timestamp(System.currentTimeMillis()));
            oldversiondoc.remove("_id");
            offsethistoryrangeupdate(mongo,oldversiondoc);
        }
    }

    public static void countrymetricupdate(String countryname , String cases , String deaths){
        MongoCollection<Document> mongocollection= setdatabasecollection("corono","countrymetric");
        totalcase = Integer.parseInt(cases.replaceAll(",",""));
        deathcase = Integer.parseInt(cases.replaceAll(",",""));
        searchQuery.append("country", countryname);
        Document aggregate_data = new Document().append("repdate", sdf.format(timestamp)).append("infectcount",totalcase).append("deathcount",deathcase);
        UpdateResult result = mongocollection.updateOne(searchQuery,Updates.addToSet("aggdata",aggregate_data));
        if(result.getModifiedCount() > 0)
        {
            System.out.println("DB updated");
        }else
        {
            Document base_doc = new Document().append("country",countryname);
            List<Document> agg_list = new ArrayList<>();
            agg_list.add(aggregate_data);
            base_doc.put("aggdata", agg_list);
            mongocollection.insertOne(base_doc);
        }
    }

    public static void offsethistoryrangeupdate(MongoClient mongo,Document oldversiondoc){
        MongoCollection<Document> mongocollection= setdatabasecollection("corono","offsethistory");
        mongocollection.insertOne(oldversiondoc);
    }

}
