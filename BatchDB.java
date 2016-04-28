
import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
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
public class BatchDB {
    
    public enum criteriasEnum{ATM, TRANSPORT, SUPERMARKET};
    public static int[] criterias = {0,1,2};
    public static int[] modeTransports = {0,1};
    
    public static void main(String[] args) {
        Calculation calc = new Calculation();
        //GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAifXyM--D-pfXTwiKzwFzxnOiCJTC0H1Y"); //Gomar
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyBh8bdjTT0HU92B7lwlRUyQ6q0X23Wfiks"); //Adrien
        
        //1. Get file OSM
            //TODO
        
        //2.init Grid
        Grid grid = new Grid();
        grid.initGrid();
        
        //3.get coordinate for each type (atm, supermarket, transports)
        List<Atm> listAtm = new ArrayList<>(); 
        List<Transport> listTrans = new ArrayList<>(); 
        List<Supermarket> listSuper = new ArrayList<>(); 
        
        List<Criteria> listTemp = new ArrayList<>();
        
        String filePath = "C:\\Users\\adrie\\Desktop\\PLD_HABITOU\\CriteriHome\\output.json";
        
        try{
            Scanner scanner = new Scanner(new File(filePath));
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
                        jsonObj = jsonObj.substring(0, jsonObj.length()-1);
                        switch (jsonObjType) {
                            case "atm" :
                                Atm atm = new Gson().fromJson(jsonObj, Atm.class);
                                listAtm.add(atm);
                            case "supermarket" :
                                Supermarket supermarket = new Gson().fromJson(jsonObj, Supermarket.class);
                                listSuper.add(supermarket);
                                break;
                        }
                    }
                }
                if(line.contains("type")){
                    line = line.replaceAll(" ", "");  
                    line = line.replaceAll("\"type\":\"", "");
                    line = line.replaceAll("\",", ""); 
                    System.out.println(line);
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
        
        //Element NewElement = reader.read(fr)
                
                
        //4.calculate distance for each square
            for(Square square : grid.listeSquare){
                Atm bestAtmW = new Atm(0,0,0, "");
                Atm bestAtmD = new Atm(0,0,0, "");
                
                Transport bestTransW = new Transport(0,0,0, "");
                Transport bestTransD = new Transport(0,0,0, "");
                
                Supermarket bestSuperW = new Supermarket(0,0,0, "");
                Supermarket bestSuperD = new Supermarket(0,0,0, "");
                
                for(criteriasEnum criteria: criteriasEnum.values()){
                    listTemp.clear();
                    switch(criteria) {
                        case ATM : 
                            for (Atm a : listAtm){listTemp.add((Criteria) a);                            }
                            break;
                        case TRANSPORT : 
                            for (Transport a : listTrans){listTemp.add((Criteria) a);                            }
                            break;
                        case SUPERMARKET : 
                            for (Supermarket a : listSuper){listTemp.add((Criteria) a);                            }
                            break;
                    } 
                    for(int modeTransport : modeTransports){
                        Criteria bestCTemps;
                        while(listTemp.size() >= 100){
                            bestCTemps = calc.nearestCriteria(context, square, listTemp.subList(0, 100), modeTransport);
                            listTemp.removeAll(listTemp.subList(0,100));
                            listTemp.add(bestCTemps);
                        }
                        bestCTemps = calc.nearestCriteria(context, square, listTemp, modeTransport);
                        switch(criteria) {
                            case ATM : 
                                switch(modeTransport){
                                    case 0 :    //DRIVING
                                        bestAtmD.setTime(bestCTemps.getTime());
                                        bestAtmD.setLat(bestCTemps.getLat());
                                        bestAtmD.setLon(bestCTemps.getLon());
                                        bestAtmD.setName(bestCTemps.getName());
                                        break;
                                    case 1 :    //WALKING
                                        bestAtmW.setTime(bestCTemps.getTime());
                                        bestAtmW.setLat(bestCTemps.getLat());
                                        bestAtmW.setLon(bestCTemps.getLon());
                                        bestAtmW.setName(bestCTemps.getName());
                                        break;
                                }
                                break;
                            case TRANSPORT : 
                                switch(modeTransport){
                                    case 0 :    //DRIVING
                                        bestTransD.setTime(bestCTemps.getTime());
                                        bestTransD.setLat(bestCTemps.getLat());
                                        bestTransD.setLon(bestCTemps.getLon());
                                        bestTransD.setName(bestCTemps.getName());
                                        break;
                                    case 1 :    //WALKING
                                        bestTransW.setTime(bestCTemps.getTime());
                                        bestTransW.setLat(bestCTemps.getLat());
                                        bestTransW.setLon(bestCTemps.getLon());
                                        bestTransW.setName(bestCTemps.getName());
                                        break;
                                }
                                break;
                            case SUPERMARKET : 
                                switch(modeTransport){
                                    case 0 :    //DRIVING
                                        bestSuperD.setTime(bestCTemps.getTime());
                                        bestSuperD.setLat(bestCTemps.getLat());
                                        bestSuperD.setLon(bestCTemps.getLon());
                                        bestSuperD.setName(bestCTemps.getName());
                                        break;
                                    case 1 :    //WALKING
                                        bestSuperW.setTime(bestCTemps.getTime());
                                        bestSuperW.setLat(bestCTemps.getLat());
                                        bestSuperW.setLon(bestCTemps.getLon());
                                        bestSuperW.setName(bestCTemps.getName());
                                        break;
                                }
                                break;
                        } 
                    }
                }
                InsertMongo im = new InsertMongo(square, bestAtmW, bestAtmD, bestTransW, bestTransD, bestSuperW, bestSuperD);
            }
    }
    
    public static List<Criteria> cloneList(List<Criteria> list) {
        List<Criteria> clone = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            clone.add(list.get(i));
        }
        return clone;  
    }
}

