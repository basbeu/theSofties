package ch.epfl.sweng.favors.location;

import android.databinding.ObservableField;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.main.FavorsMain;
import ch.epfl.sweng.favors.utils.ExecutionMode;

/**
 * LocationHandler
 * Owns all request and management facilities related to location
 *
 * can measure distance other geopoints which is important for displaying
 * how far favors are away
 *
 * can set the location to be tracked continuously or only after opening the app & intervals
 *
 * get a human readable address from coordinates
 *
 * the difference between a geopoint and a Location Object is that a LOcation object is
 * rich in additional methods, provides elevation and distance methods whereas a geopoint
 * is essentially just latitude and longitude information
 *
 * The Geopoint can however be natively handled by firestore and is thus the preferred choice
 * in database interactions
 */
public class LocationHandler {
    private static final String TAG = "LOCATION_HANDLER";

    private static LocationHandler handler = new LocationHandler(true);
    private static LocationHandler handlerNonRec = new LocationHandler(false);
    public static LocationHandler getHandler(){
        return handler;
    }

    // overload getHandler in order to provide method to get non recurrent handler
    public static LocationHandler getHandler(Boolean recurrent){
        if(recurrent) {
            return handler;
        } else {
            return handlerNonRec;
        }
    }

    /**
     * holds the user's last location
     */
    private Location lastLocation;
    public ObservableField<String> locationCity = new ObservableField<>();
    public ObservableField<GeoPoint> locationPoint = new ObservableField<>();
    public ObservableField<Location> locationUser = new ObservableField<>();

    protected ch.epfl.sweng.favors.location.Location location = ch.epfl.sweng.favors.location.Location.getInstance();

    private boolean recurrent = false;

    public static float distanceTo(GeoPoint geo) {
        Location favLocation = new Location("favor");
        float distance = Float.MAX_VALUE;

        Location l = LocationHandler.getHandler().locationUser.get();
        if(l != null && geo != null) {
            favLocation.setLatitude(geo.getLatitude());
            favLocation.setLongitude(geo.getLongitude());
            distance = l.distanceTo(favLocation);
            Log.d("Location Debug", "Distance: " + distance + ", FavorLocation " + favLocation.getLatitude()+", "+favLocation.getLongitude() + ", UserLocation " +l.getLatitude()+","+l.getLongitude());
        }
        return distance;
    }

    /**
     * This method allows to get the distance between the user's current location and some
     * arbitrary geopoint.
     *
     * values above 100km are integers
     * values between 100km and 2.5km have one decimal
     * values below 2.5km are displayed in meters
     *
     * @param geo (latitude, longitude)
     * @return a properly formatted string representing the distance to the geopoint
     */
    public static String distanceBetween(GeoPoint geo) {
        float distance = distanceTo(geo);
        String output;
        int switchToMeters = 2500;
        int switchToInt = 100000;
        if (distance == Float.MAX_VALUE) {
            return "There is no Location";
        } else if (distance > switchToInt) {
            output = String.format(Locale.getDefault(), "%.0f", (distance/1000)) + " km";
        } else if (distance > switchToMeters) {
            output = String.format(Locale.getDefault(), "%.1f", (distance/1000)) + " km";
        } else {
            if(distance == 0){
                return  "Near you";
            }else output = String.format(Locale.getDefault(), "%.0f", distance) + " m";
        }
        return output + " away";
    }

    /**
     * Sets the location update frequency to either be recurrent or just once per app launch
     * @param newValue boolean recurrent
     */
    public void isRecurrent(boolean newValue){
        if (!newValue && recurrent) {
            location.removeLocationUpdates();
        }
        if (newValue && !recurrent) {
            location.requestLocationUpdates(locationRequest(), successListerner);
        }
        recurrent = newValue;
    }


    public void permissionFeedback(){
        if(recurrent) {
            location.requestLocationUpdates(locationRequest(), successListerner);
        }
        else updateLocation();
    }


    /**
     * Issues a request to the location client to produce regular location updates
     * @return a request with the specifics below
     */
    private LocationRequest locationRequest(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(60 * 1000); // 60 seconds
        locationRequest.setFastestInterval(30 * 1000); // 30 seconds
        return locationRequest;
    }


    public LocationHandler(boolean recurrent){
        isRecurrent(recurrent);
    }

    OnSuccessListener<Location> successListerner = location -> updateLocationInformations(location);

    public void updateLocation() {
        location.getLastLocation(successListerner);
    }


    private boolean updateLocationInformations(Location l){
        if (l == null) {
            Log.e(TAG, "Location object is missing in location update request");
            return false;
        }

        lastLocation = l;
        locationPoint.set(new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));
        locationCity.set(getReadableLocation(locationPoint.get()));
        locationUser.set(lastLocation);
        User.getMain().setLocation(locationPoint.get());
        // if desired the user city can automatically be updated every time location changes
        // User.setCity(locationCity.get());
        Log.d("location", "code:1000 - successfully obtained location of user");
        return true;
    }

    /**
     * Tries to resolve a geopoint to a physical address like humans use them
     *
     * @param geoPoint which is resolved to an address
     * @return String of the address corresponding to the geopoint or error
     */
    private String getReadableLocation(GeoPoint geoPoint){

        if (geoPoint == null) {
            Log.e(TAG, "Location geopoint does not have any content");
            return "Not available";
        }
        if(!ExecutionMode.getInstance().isTest()) {
            Geocoder gcd = new Geocoder(FavorsMain.getContext(), Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
                if (addresses.size() > 0) return addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryCode();
            } catch (IOException e) {
                Log.e(TAG, "Failed to get geoPoint information");
            }
            return "Not available";
        }
        else{
            return  "Fake Lausanne";
        }
    }
}