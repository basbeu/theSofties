package ch.epfl.sweng.favors.location;

import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import java.util.Comparator;
import ch.epfl.sweng.favors.database.Favor;

public class SortLocations implements Comparator<Favor> {
    GeoPoint currentLoc;

    public SortLocations(GeoPoint current){
        currentLoc = current;
    }
    @Override
    public int compare(final Favor f1, final Favor f2) {

        GeoPoint place1 = (GeoPoint) f1.get(Favor.ObjectFields.location);
        GeoPoint place2 = (GeoPoint) f2.get(Favor.ObjectFields.location);

        if(place1 != null && place2 != null){
            double lat1 = place1.getLatitude();
            double lon1 = place1.getLongitude();
            double lat2 = place2.getLatitude();
            double lon2 = place2.getLongitude();

            double distanceToPlace1 = distance(currentLoc.getLatitude(), currentLoc.getLongitude(), lat1, lon1);
            double distanceToPlace2 = distance(currentLoc.getLatitude(), currentLoc.getLongitude(), lat2, lon2);
            return (int) (distanceToPlace1 - distanceToPlace2);
        }
        else return 0;

    }

    public double distance(double fromLatDeg, double fromLonDeg, double toLatDeg, double toLonDeg) {
        double fromLat = Math.toRadians(fromLatDeg);
        double fromLon = Math.toRadians(fromLonDeg);
        double toLat = Math.toRadians(toLatDeg);
        double toLon = Math.toRadians(toLonDeg);

        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }
}
