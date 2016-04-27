/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author adrie
 */
public class Square {
    private float lat;
    private float lon;

    public Square(float x, float y ){
        this.lat = x;
        this.lon = y;
    }
    
    public String getPosition(){
        String s = Float.toString(lat) + ", " + Float.toString(lon); 
        return s;
    }
    
    public float getLat(){
        return this.lat;
    }
    
    public float getLon(){
        return this.lon;
    }
        
    public void setLat(float lat){
        this.lat = lat;
    }
    
    public void setLon(float lon){
        this.lon = lon;
    }
}
