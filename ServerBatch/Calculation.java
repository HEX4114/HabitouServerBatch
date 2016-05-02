package ServerBatch;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Route;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adrie
 */

public class Calculation {
    
    public Criteria nearestCriteriaGoogle(GeoApiContext context, Square origin, List<Criteria> criterias, int mode){
        //on envoie une liste de 100 criteria
        Criteria nearCriteria = criterias.get(0);
        String[] positionsCriteria = new String[criterias.size()];
        for(int i = 0; i < criterias.size(); i++){
            positionsCriteria[i] = criterias.get(i).getPositionGoogle();
        }
         long [] times = calculationTimeSquareGoogle(context, origin.getPositionGoogle(), positionsCriteria, mode);
         long minT = times[0];
         int minIndex = 0;
         for (int i = 0; i< times.length; i++)
         {
             if (times[i]<minT) 
             {
                 minT = times[i];
                 minIndex = i;
             }
         }
        criterias.get(minIndex).setTime(times[minIndex]);
        nearCriteria = criterias.get(minIndex);
        return nearCriteria;
    }
    
    public Criteria nearestCriteriaOsrm(GeoApiContext context, Square origin, List<Criteria> criterias, int mode,  Criteria bestWalkingCriteria) throws IOException{
        Criteria nearCriteria = criterias.get(0);
        
         List<Criteria> criteriasCalculated = calculationTimeSquareOsrm(context, origin, criterias, mode, bestWalkingCriteria);
         float minT = criteriasCalculated.get(0).getTime();
         int minIndex = 0;
         for (int i = 0; i< criteriasCalculated.size(); i++)
         {
             if (criteriasCalculated.get(i).getTime()<minT) 
             {
                 minT = criteriasCalculated.get(i).getTime();
                 minIndex = i;
             }
         }
        criterias.get(minIndex).setTime(criteriasCalculated.get(minIndex).getTime());
        criterias.get(minIndex).setDistance(criteriasCalculated.get(minIndex).getDistance());
        nearCriteria = criterias.get(minIndex);
        return nearCriteria;
    }
    
    public List<Criteria> calculationTimeSquareOsrm(GeoApiContext context, Square origin, List<Criteria> destinations, int mode, Criteria bestWalkingCriteria) throws MalformedURLException, IOException{
        if(bestWalkingCriteria.getTime() < 180 && mode==1)
        {
            List<Criteria> dest = new ArrayList<>();
            dest.add(bestWalkingCriteria);
            return dest;
        }
        
        String[] positionsCriteria = new String[destinations.size()];
        for(int i = 0; i < destinations.size(); i++){
            positionsCriteria[i] = destinations.get(i).getPositionOsrm();
        }
        
        // issue the Get request
        RequestServer requestServer = new RequestServer();
        String getResponse = "";
        for(int i = 0; i < destinations.size(); i++)
        {
            String request = GetRequest(mode, origin.getPositionOsrm(), positionsCriteria[i]);
            getResponse = requestServer.doGetRequest(request);
            //TODO : parser to obtain duration for each destination and put it into distances
            float duration = getInfos(getResponse, "duration");
            float distance = getInfos(getResponse, "distance");
            destinations.get(i).setTime(duration);
            destinations.get(i).setDistance(distance);
            
        }
        
        return destinations;
    }
    
    public float getInfos(String responseServer, String info)
    {
        float response = 0f;
        String responsePart = "";
        String[] responseParsed = responseServer.split(",");
        for(int i = 0; i < responseParsed.length; i++)
        {
            if(responseParsed[i].contains("\""+info+"\":"))
            {
                responsePart = responseParsed[i].split(":")[1];
                if(info == "distance")
                {
                    responsePart = responsePart.substring(0, responsePart.length()-2);
                }
            }
        }
        response = Float.parseFloat(responsePart);
        
        return response;
    }
    
    public String GetRequest(int mode, String origin, String destination)
    {
        String port = "";
        if(mode == 0)
        {
            port = "5001";
        }
        else if (mode == 1) 
        {
            port = "5000";
        }
        String request = "http://192.168.1.2:" + port + "/route/v1/driving/" + origin + ";" + destination;
        return request;
    }
    
    public long[] calculationTimeSquareGoogle(GeoApiContext context, String origin, String[] destinations, int mode){
        long[] listeDistances = new long[destinations.length];
        
        DistanceMatrix matrix = null;
        try {
            switch (mode) {
                case 0 :         
                    matrix = DistanceMatrixApi.newRequest(context)
                                .origins(origin)
                                .destinations(destinations)
                                .mode(TravelMode.DRIVING)
                                .units(Unit.METRIC)
                                .await();
                    break;
                case 1 :         
                    matrix = DistanceMatrixApi.newRequest(context)
                                .origins(origin)
                                .destinations(destinations)
                                .mode(TravelMode.WALKING)
                                .units(Unit.METRIC)
                                .await();
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(BatchDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0; i < matrix.rows.length; i++){
            for(int j = 0; j < matrix.rows[i].elements.length; j++){
                if(matrix.rows[i].elements[j].duration!=null)
                {
                    listeDistances[j] = matrix.rows[i].elements[j].duration.inSeconds;
                } else
                {
                    listeDistances[j] = 30000000;
                }
               
            }
        }
        return listeDistances;
    }
    
    public static final float R = 6372.8f;
    public float haversine(float lat1, float lon1, float lat2, float lon2) {
        float dLat = (float)Math.toRadians(lat2 - lat1);
        float dLon = (float)Math.toRadians(lon2 - lon1);
        lat1 = (float)Math.toRadians(lat1);
        lat2 = (float)Math.toRadians(lat2);
 
        float a = (float) (Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2));
        float c = (float)(2 * Math.asin(Math.sqrt(a)));
        return R * c;
    }
    
    public float calculationTimeBird(Square origin, Square destination)
    {
        float latOri = origin.getLat();
        float lonOri = origin.getLon();
        float latDes = destination.getLat();
        float lonDes = destination.getLon();
        return haversine(latOri, lonOri, latDes, lonDes);
        
    }
    
}
