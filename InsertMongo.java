
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adrie
 */
public class InsertMongo {
    public InsertMongo(Square square, Criteria bestAtmW, Criteria bestAtmD, 
                                      Criteria bestTransW, Criteria bestTransD, 
                                      Criteria bestSuperW, Criteria bestSuperD){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("habitoudb");

        FindIterable<Document> iterable = db.getCollection("Habitou").find();

        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) 
            {
                System.out.println(document);
            }  
        });   
        db.getCollection("squares").insertOne(
                new Document("lat", square.getLat())
                .append("long", square.getLon())
                .append("atm",
                        new Document()
                        .append("walk", 
                                new Document()
                                .append("time", bestAtmW.getTime())
                                .append("lat", bestAtmW.getLat())
                                .append("long", bestAtmW.getLon())
                        )
                        .append("drive", 
                                new Document()
                                .append("time", bestAtmD.getTime())
                                .append("lat", bestAtmD.getLat())
                                .append("long", bestAtmD.getLon())
                        )
                )
                .append("supermarket",
                        new Document()
                        .append("walk", 
                                new Document()
                                .append("time", bestSuperW.getTime())
                                .append("lat", bestSuperW.getLat())
                                .append("long", bestSuperW.getLon())
                        )
                        .append("drive", 
                                new Document()
                                .append("time", bestSuperD.getTime())
                                .append("lat", bestSuperD.getLat())
                                .append("long", bestSuperD.getLon())
                        )
                )
                .append("transport",
                        new Document()
                        .append("walk", 
                                new Document()
                                .append("time", bestTransW.getTime())
                                .append("lat", bestTransW.getLat())
                                .append("long", bestTransW.getLon())
                        )
                        .append("drive", 
                                new Document()
                                .append("time", bestTransD.getTime())
                                .append("lat", bestTransD.getLat())
                                .append("long", bestTransD.getLon())
                        )
                )
        );
    }
}
