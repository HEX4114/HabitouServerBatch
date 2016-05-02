package ServerBatch;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Isil
 */
public class AirRhoneParser {
    
    private String path = "";
    private String fileName = "";
    
    public AirRhoneParser(String path, String fileName)
    {
        this.path = path;
        this.fileName = fileName;
    }
    
    public List<StationPollution> parse() throws FileNotFoundException
    {
        List<StationPollution> listPol = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path + File.separator + fileName));
        String line = scanner.nextLine();
        while(scanner.hasNext())
        {
            line = scanner.nextLine();
            float lat = getLat(line);
            float lon = getLong(line);
            String name = getName(line);
            float polRate = getPolRate(line);
            StationPollution staPol = new StationPollution(lat, lon, name, polRate, 0f);
            listPol.add(staPol);
        }
        
        return listPol;
    }

    private float getLat(String line) {
        String slat = line.split(",")[0];
        slat = slat.replace("\"", "");
        float flat = Float.parseFloat(slat);
        return flat;
    }

    private float getLong(String line) {
        String slon = line.split(",")[1].split(";")[0];
        slon = slon.replace("\"", "");
        float flon = Float.parseFloat(slon);
        return flon;
    }

    private String getName(String line) {
        String name = line.split(";")[1];
        return name;
    }

    private float getPolRate(String line) {
        float rate = 0;
        int monthMissed = 0;
        String [] lineParsed = line.split(";");
        for(int i = 5; i < lineParsed.length; i++)
        {
            if(lineParsed[i].equals("-"))
            {
                monthMissed++;
            }
            else 
            {
                rate += Float.parseFloat(lineParsed[i]);
            }
        }
        rate = rate / (lineParsed.length-5-monthMissed);
        return rate;
    }
    
}
