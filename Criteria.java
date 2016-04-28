/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adrie
 */
public abstract class Criteria extends Square{
    
    private float time;
    private String name;
    
    public Criteria(float x, float y, float time, String name) {
        super(x, y);
        this.time = time;
        this.name = name;
    }
    
    public void setTime(float t){
        this.time = t;
    }
    
    public float getTime(){
        return this.time;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
}
