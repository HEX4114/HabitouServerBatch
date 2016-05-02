package ServerBatch;


import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
public class BatchDB {
    
    public enum criteriasEnum{ATM, TRANSPORT, SUPERMARKET};
    public static int[] criterias = {0,1,2};
    public static int[] modeTransports = {0,1}; //0=W & 1=D
    private static Calculation calc = new Calculation();
    //private static GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyBh8bdjTT0HU92B7lwlRUyQ6q0X23Wfiks");  //A
    private static GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyArobVSwthXEPCYJFsepnC0yRz13ER9EQU");    //F
    //private static GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyDnq20kQLytsbe5Idn4qL-2HoJ2mLwL5v4");  //M
    
    public static void main(String[] args) throws IOException {
        
        /***** TEST *****/
        String csvFileName = "station_particules-pm10_4.csv";
        File file = new File("");
        String csvFilePath = file.getAbsolutePath();
        AirRhoneParser airParser = new AirRhoneParser(csvFilePath, csvFileName);
        airParser.parse();
         
         
         

        
        
        
        //--Init Mongo Database client
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("habitoudb");
        
        //1. Get Jsonfile from OSM
        String jsonFileName = "output.json";
        String jsonFilePath = file.getAbsolutePath() + File.separator + jsonFileName;
        
        //2.init Grid
        Grid grid = new Grid();
        grid.initGrid();
        System.out.println(grid.listeSquare.size());
        
        //3.Create and add object into List from JsonFile
        List<Atm> listAtm = new ArrayList<>(); 
        List<Supermarket> listSuper = new ArrayList<>(); 
        
        try{
            Scanner scanner = new Scanner(new File(jsonFilePath));
            String jsonObj = "";
            String jsonObjType = "";
            String line = scanner.nextLine();
            
            while(!line.contains("{"))
            {
                line = scanner.nextLine();
            }
            int nbBracket = 0;
            while (scanner.hasNextLine()) 
            {
                
                jsonObj += line;                
                
                if(line.contains("{"))
                {
                    nbBracket++;
                } else if (line.contains("}"))
                {
                    nbBracket--;
                    if(nbBracket == 0)
                    {
                        if(jsonObj.endsWith(","))
                        {
                            jsonObj = jsonObj.substring(0, jsonObj.length()-1);
                        }
                        switch (jsonObjType) {
                            case "atm" :
                                Atm atm = new Gson().fromJson(jsonObj, Atm.class);
                                listAtm.add(atm);
                            case "supermarket" :
                                Supermarket supermarket = new Gson().fromJson(jsonObj, Supermarket.class);
                                listSuper.add(supermarket);
                                break;
                        }
                        jsonObj = "";
                        jsonObjType = "";
                    }
                }
                if(line.contains("type")){
                    line = line.replaceAll(" ", "");  
                    line = line.replaceAll("\"type\":\"", "");
                    line = line.replaceAll("\",", ""); 
                    if (line.equals("atm"))
                    {
                        jsonObjType = "atm";
                    } else if (line.equals("supermarket"))
                    {
                        jsonObjType = "supermarket";
                    }
                }
                line = scanner.nextLine();
                
            }   
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        //TEST réécriture 4.
        List<Criteria> listTemp = new ArrayList<>();
        for(Square square : grid.listeSquare)
        {
            Atm bestAtmW = new Atm(0,0,0,0,"");
            Atm bestAtmD = new Atm(0,0,0,0,"");

            Supermarket bestSuperW = new Supermarket(0,0,0,0, "");
            Supermarket bestSuperD = new Supermarket(0,0,0,0, "");
            
            for(criteriasEnum criteria: criteriasEnum.values()){
                listTemp.clear();
                    switch(criteria) {
                        case ATM :
                            for (Atm a : listAtm){listTemp.add((Criteria) a);}
                            listTemp = reduceListForSquare(square, listTemp);
                            
                            for(int modeTransport : modeTransports){
//                              Criteria bestCTemps = getBestCTimeGoogle(square, listTemp, modeTransport); 
                                Criteria bestCTemps = getBestCTimeOsrm(square, listTemp, modeTransport, bestAtmW); 
                                switch(modeTransport){
                                    case 0 :    //Walking
                                        bestAtmW.setTime(bestCTemps.getTime());
                                        bestAtmW.setLat(bestCTemps.getLat());
                                        bestAtmW.setLon(bestCTemps.getLon());
                                        bestAtmW.setName(bestCTemps.getName());
                                        bestAtmW.setDistance(bestCTemps.getDistance());
                                        break;
                                    case 1 :    //Driving
                                        bestAtmD.setTime(bestCTemps.getTime());
                                        bestAtmD.setLat(bestCTemps.getLat());
                                        bestAtmD.setLon(bestCTemps.getLon());
                                        bestAtmD.setName(bestCTemps.getName());
                                        bestAtmD.setDistance(bestCTemps.getDistance());

                                        break;
                                }
                            }
                            break;
                        case SUPERMARKET : 
                            for (Supermarket a : listSuper){listTemp.add((Criteria) a);}
                            listTemp = reduceListForSquare(square, listTemp);
                            for(int modeTransport : modeTransports){
//                              Criteria bestCTemps = getBestCTimeGoogle(square, listTemp, modeTransport); 
                                Criteria bestCTemps = getBestCTimeOsrm(square, listTemp, modeTransport, bestSuperW); 
                                switch(modeTransport){
                                    case 0 :    //Walking
                                        bestSuperW.setTime(bestCTemps.getTime());
                                        bestSuperW.setLat(bestCTemps.getLat());
                                        bestSuperW.setLon(bestCTemps.getLon());
                                        bestSuperW.setName(bestCTemps.getName());
                                        bestSuperW.setDistance(bestCTemps.getDistance());
                                        break;
                                    case 1 :    //Driving
                                        bestSuperD.setTime(bestCTemps.getTime());
                                        bestSuperD.setLat(bestCTemps.getLat());
                                        bestSuperD.setLon(bestCTemps.getLon());
                                        bestSuperD.setName(bestCTemps.getName());
                                        bestSuperD.setDistance(bestCTemps.getDistance());

                                        break;
                                }
                            }
                            break;
                    }
            }
            InsertMongo im = new InsertMongo(db, square, bestAtmW, bestAtmD, bestSuperW, bestSuperD);
        }
    }
    
    public static List<Criteria> cloneList(List<Criteria> list) {
        List<Criteria> clone = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            clone.add(list.get(i));
        }
        return clone;  
    }
    
    public static List<Criteria> reduceListForSquare (Square square,List<Criteria> criterias)
    {
        float minDist = calc.calculationTimeBird(square, criterias.get(0));
        for(int i = 0; i < criterias.size(); i++)
        {
            float currentDist = calc.calculationTimeBird(square, criterias.get(i));
            if(currentDist > minDist*2)
            {
                criterias.remove(i);
                i--;
            } else if(currentDist < minDist)
            {
                minDist = currentDist;
            }
        }
        
        return criterias;
    }
    
    public static Criteria getBestCTimeOsrm(Square square, List<Criteria> listTemp, int modeTransport, Criteria bestWalkingCriteria) throws IOException
    {
        Criteria bestCDis = listTemp.get(0);
        float minDist = calc.calculationTimeBird(square, listTemp.get(0));
        for(int i = 0; i < listTemp.size(); i++)
        {
            float currentDist = calc.calculationTimeBird(square, listTemp.get(i));
            if(currentDist < minDist)
            {
                minDist = currentDist;
                bestCDis = listTemp.get(i);
            }
        }
        List<Criteria> bestCriteria = new ArrayList<>();
        bestCriteria.add(bestCDis);
        bestCDis = calc.nearestCriteriaOsrm(context, square, bestCriteria, modeTransport, bestWalkingCriteria);
        return bestCDis;
    }
    
    public static Criteria getBestCTimeGoogle(Square square, List<Criteria> listTemp, int modeTransport)
    {
        Criteria bestCTime;
        while(listTemp.size() >= 100)
        {
            bestCTime = calc.nearestCriteriaGoogle(context, square, listTemp.subList(0, 100), modeTransport);
            listTemp.removeAll(listTemp.subList(0,100));
            listTemp.add(bestCTime);
        }
        bestCTime = calc.nearestCriteriaGoogle(context, square, listTemp, modeTransport);
        
        return bestCTime;
    }
}

