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
    float nLong;
    float nLat;
    float lat1;
    float long1;
    float lat2; 
    float long2;
    
    //--Grand Lyon
    //float nLat = 0.005f;      //Lat
    //float nLong = 0.0075f;    //Long 
    //--Zoom Lyon
    //float nLat = 0.0005f;     //Lat
    //float nLong = 0.00075f;   //Long 
    
    public Grid(){
        this.nLong = 0f;
        this.nLat = 0f;
        this.lat1 = 0f;
        this.long1 = 0f;
        this.lat2 = 0f; 
        this.long2 = 0f; 
    }
    
    public void setLat1(float lat1){
        this.lat1 =lat1;
    }
    public void setLat2(float lat2){
        this.lat2 =lat2;
    }
    public void setLong1(float long1){
        this.long1 =long1;
    }
    public void setLong2(float long2){
        this.long2 =long2;
    }
    public void setNLat(float nLat){
        this.nLat =nLat;
    }
    public void setNLong(float nLong){
        this.nLong =nLong;
    }
    
    public void initGrid(){
        //Latitude
        //for(float i=45.74f; i<45.78005f; i+=nLat) //zoom Lyon
        //for(float i=45.56f; i<45.92f; i+=nLat)    //Grand Lyon
        for(float i=lat1; i<lat2; i+=nLat)
        {
            //Longitude
            //for(float j=4.81f; j<4.87005f; j+=nLong)  //zoom sur Lyon 
            //for(float j=4.66f; j<5.07f; j+=nLong)     //Grand Lyon
            for(float j=long1; j<long2; j+=nLong)       
            {
                listSquare.add(new Square(i+nLat/2,j+nLong/2));
            }
        }        
    }
}
