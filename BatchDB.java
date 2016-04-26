
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public static void main(String[] args) {
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAifXyM--D-pfXTwiKzwFzxnOiCJTC0H1Y");
        String origin = "45.93, 4.66";
        String[] destinations = new String[] {"45.93, 5.06", "45.56, 4.66"};
        
        long[] listeDistances = calculDistanceCase(context, origin, destinations);
        for(int i = 0; i < listeDistances.length; i++){
            System.out.println(listeDistances[i]);
        }
    }
    
    public static long[] calculDistanceCase(GeoApiContext context, String origin, String[] destinations){
        System.out.println("---- CALCUL DISTANCE ----");
        long[] listeDistances = new long[destinations.length];
        
        DistanceMatrix matrix = null;
        try {
            matrix = DistanceMatrixApi.newRequest(context)
                    .origins(origin)
                    .destinations(destinations)
                    .mode(TravelMode.WALKING)
                    .units(Unit.METRIC)
                    .await();
        } catch (Exception ex) {
            Logger.getLogger(BatchDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0; i < matrix.rows.length; i++){
            for(int j = 0; j < matrix.rows[i].elements.length; j++){
                System.out.println("distance :"+matrix.rows[i].elements[j].distance.inMeters);
                listeDistances[j] = matrix.rows[i].elements[j].distance.inMeters;
            }
        }
        
        return listeDistances;
    }
}
