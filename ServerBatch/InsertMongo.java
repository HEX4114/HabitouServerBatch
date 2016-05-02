package ServerBatch;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adrie
 */

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;




 public class InsertMongo {
    public InsertMongo(MongoDatabase db ,Square square, Criteria bestAtmW, Criteria bestAtmD, 
                                      Criteria bestSuperW, Criteria bestSuperD, StationPollution bestStaPol){

        /*FindIterable<Document> iterable = db.getCollection("squares").find();

        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) 
            {
                System.out.println(document);
            }  
        }); */
        
        db.getCollection("squares").insertOne(
                new Document("lat", square.getLat())
                .append("long", square.getLon())
                .append("atm",
                        new Document()
                        .append("walk", 
                                new Document()
                                .append("name", bestAtmW.getName())
                                .append("time", bestAtmW.getTime())
                                .append("lat", bestAtmW.getLat())
                                .append("long", bestAtmW.getLon())
                                .append("distance", bestAtmW.getDistance())
                                
                        )
                        .append("drive", 
                                new Document()
                                .append("name", bestAtmD.getName())
                                .append("time", bestAtmD.getTime())
                                .append("lat", bestAtmD.getLat())
                                .append("long", bestAtmD.getLon())
                                .append("distance", bestAtmD.getDistance())
                                
                        )
                )
                .append("supermarket",
                        new Document()
                        .append("walk", 
                                new Document()
                                .append("name", bestSuperW.getName())
                                .append("time", bestSuperW.getTime())
                                .append("lat", bestSuperW.getLat())
                                .append("long", bestSuperW.getLon())
                                .append("Distance", bestSuperW.getDistance())
                        )
                        .append("drive", 
                                new Document()
                                .append("name", bestSuperD.getName())
                                .append("time", bestSuperD.getTime())
                                .append("lat", bestSuperD.getLat())
                                .append("long", bestSuperD.getLon())
                                .append("distance", bestSuperD.getDistance())
                        )
                )
                .append("pollution",
                            new Document()
                            .append("name", bestStaPol.getName())
                            .append("rate", bestStaPol.getPolRate())
                            .append("lat", bestStaPol.getLat())
                            .append("long", bestStaPol.getLon())
                            .append("distance", bestStaPol.getDistance())
                        
                )
        );
        //System.out.println("ok :"+square.toString());
    }
}
