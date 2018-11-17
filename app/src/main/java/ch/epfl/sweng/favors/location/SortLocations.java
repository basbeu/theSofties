package ch.epfl.sweng.favors.location;

import com.google.firebase.firestore.GeoPoint;

import java.util.Comparator;

public class SortLocations implements Comparator<GeoPoint> {
    GeoPoint currentLoc;

    public SortLocations(GeoPoint current){
        currentLoc = current;
    }
    @Override
    public int compare(final GeoPoint place1, final GeoPoint place2) {
        double lat1 = place1.getLatitude();
        double lon1 = place1.getLongitude();
        double lat2 = place2.getLatitude();
        double lon2 = place2.getLongitude();
        double distanceToPlace1 = distance(currentLoc.getLatitude(), currentLoc.getLongitude(), lat1, lon1);
        double distanceToPlace2 = distance(currentLoc.getLatitude(), currentLoc.getLongitude(), lat2, lon2);
        return (int) (distanceToPlace1 - distanceToPlace2);
    }

    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
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
