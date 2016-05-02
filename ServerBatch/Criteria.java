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
public class Criteria extends Square{
    
    private float time;
    private float distance;
    private String name;
    
    public Criteria(float x, float y, float time, float distance, String name) {
        super(x, y);
        this.time = time;
        this.name = name;
        this.distance = distance;
    }
    
    public void setTime(float t){
        this.time = t;
    }
    
    public float getTime(){
        return this.time;
    }
    
    public void setDistance(float d){
        this.distance = d;
    }
    
    public float getDistance(){
        return this.distance;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
}
