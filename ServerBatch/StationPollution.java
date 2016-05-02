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
    private float pollutionRate;
    
    public StationPollution(float x, float y, String name, float pollutionRate)
    {
        super(x, y);
        this.name = name;
        this.pollutionRate = pollutionRate;
    }
}
