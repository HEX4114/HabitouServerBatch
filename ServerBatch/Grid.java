package ServerBatch;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author adrien
 */



public class Grid {

    List<Square> listSquare = new ArrayList<Square>();  
    float nLat = 0.001f; //Lat
    float nLong = 0.0015f; //Long 
//    float nLat = 0.0005f; //Lat
//    float nLong = 0.00075f; //Long 
        
        /**
         * Carré du grand lyon
         * point NO : 45.93, 4.66 
         * point NE : 45.93, 5.06
         * point SE : 45.56, 5.06
         * point SO : 45.56, 4.66
         */       
    
    public void initGrid(){
        //Latitude
        for(float i=45.74f; i<45.78005f; i+=nLat) //zoom sur Lyon
        //for(float i=45.56f; i<45.92f; i+=nLat)      //Grand Lyon
        {
            //Longitude
            for(float j=4.81f; j<4.87005f; j+=nLong)  //zoom sur Lyon     
            //for(float j=4.66f; j<5.07f; j+=nLong)       //Grand Lyon
            {
                listSquare.add(new Square(i+nLat/2,j+nLong/2));
            }
        }        
    }
}
