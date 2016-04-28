
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
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
    
    public Criteria nearestCriteria(GeoApiContext context, Square origin, List<Criteria> criterias, int mode){
        //on envoie une liste de 100 criteria
        Criteria nearCriteria = criterias.get(0);
        String[] positionsCriteria = new String[criterias.size()];
        for(int i = 0; i < criterias.size(); i++){
            positionsCriteria[i] = criterias.get(i).getPosition();
        }
         long [] times = calculationTimeSquare(context, origin.getPosition(), positionsCriteria, mode);
         //TODO : get le min et l'index pour connaitre le criteria
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
    
    public long[] calculationTimeSquare(GeoApiContext context, String origin, String[] destinations, int mode){
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
    
    public float calculationTime(Square origin, Criteria destination)
    {
        float latOri = origin.getLat();
        float lonOri = origin.getLon();
        float latDes = destination.getLat();
        float lonDes = destination.getLon();
        return haversine(latOri, lonOri, latDes, lonDes);
        
    }
    
}
