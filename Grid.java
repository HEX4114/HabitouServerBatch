/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;

//Import Mongo
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import org.bson.Document;

/**
 *
 * @author adrien
 */
public class Grid {

    List<Square> listeSquare = new ArrayList<Square>();  
    float nLat = 0.01f; //Lat
    float nLong = 0.01f; //Long 
        
        /**
         * Carré du grand lyon
         * point NO : 45.93, 4.66 
         * point NE : 45.93, 5.06
         * point SE : 45.56, 5.06
         * point SO : 45.56, 4.66
         */       
    
    public void initGrid(){
        for(float i=45.74f; i<45.78f; i+=nLat)         //GRAND LYON(float i=45.56f; i<45.92f; i+=nLat)       //Latitude
        {
            for(float j=4.81f; j<4.87f; j+=nLong)      //GRAND LYON(float j=4.66f; j<5.07f; j+=nLong)    //Longitude
            {
                listeSquare.add(new Square(i,j));
            }
        }        
    }
}
