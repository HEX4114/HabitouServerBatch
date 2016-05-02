package ServerBatch;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Isil
 */
public class StationPollution extends Square{
    
    private String name;
    private float distance;
    private float pollutionRate;
    
    public StationPollution(float x, float y, String name, float pollutionRate, float distance)
    {
        super(x, y);
        this.name = name;
        this.pollutionRate = pollutionRate;
        this.distance =distance;
    }
    
    public void setPolRate(float pollutionRate){
        this.pollutionRate = pollutionRate;
    }
    
    public float getPolRate(){
        return this.pollutionRate;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setDistance(float d){
        this.distance = d;
    }
    
    public float getDistance(){
        return this.distance;
    }
}
