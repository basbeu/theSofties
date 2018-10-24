package ch.epfl.sweng.favors;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Owns all request and management facilities related to location
 */
public class LocationHandler {
    private static final String TAG = "LOCATION_HANDLER";

    private static LocationHandler handler = new LocationHandler(true);
    public static LocationHandler getHandler(){
        return handler;
    }

    private Location lastLocation;
    public ObservableField<String> locationCity = new ObservableField<>();
    public ObservableField<GeoPoint> locationPoint = new ObservableField<>();

    private FusedLocationProviderClient mFusedLocationClient;

    private boolean recurrent = false;

    public void isRecurrent(boolean newValue){
        if (checkPermission()) {
            if (!newValue && recurrent) {
                mFusedLocationClient.removeLocationUpdates(locationCallback);
            }
            if (newValue && !recurrent) {
                mFusedLocationClient.requestLocationUpdates(locationRequest(), locationCallback, null);
            }
        }
        recurrent = newValue;
    }



    private LocationRequest locationRequest(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 60 seconds
        locationRequest.setFastestInterval(5 * 1000); // 30 seconds
        return locationRequest;
    }

    /**
     * this method checks if we have the permission to access a user's location
     * @return if we are allowed (boolean)
     */
    private boolean requestPermission() {
        if(!checkPermission()){
            ActivityCompat.requestPermissions((Activity) FavorsMain.getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    FavorsMain.Permissions.LOCATION_SERVICE.ordinal());
            return false;
        }
        return true;
    }

    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(FavorsMain.getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(FavorsMain.getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public LocationHandler(boolean recurrent){
        if(!ExecutionMode.getInstance().isTest()){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(FavorsMain.getContext());
        }
        if (requestPermission()) {
            isRecurrent(recurrent);
        }
        else{
            this.recurrent = recurrent;
        }
    }

    public void permissionFeedback(){
        if(recurrent) {
            if(checkPermission()) {
                mFusedLocationClient.requestLocationUpdates(locationRequest(), locationCallback, null);
            }
        }
        else updateLocation();
    }

    /**
     * Get Location
     * Helper method for obtaining a location
     * adds a listener to LocationClient and if there is no location invokes a callback
     * @param callback for Location
     * @return the location value that was obtained (value can be ignored)
     */
    public void updateLocation() {
        if(checkPermission()) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener( l -> {
                updateLocationInformations(l);
            });
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(TAG, "CB called");
            if (locationResult == null) {
                Log.e(TAG, "No location provided by GPS");
                return;
            }
            for (Location l : locationResult.getLocations()) {
                updateLocationInformations(l);
            }
        }
    };
    private boolean updateLocationInformations(Location l){
        if (l == null) {
            Log.e(TAG, "Location object is missing in location update request");
            return false;
        }

        lastLocation = l;
        locationPoint.set(new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));
        locationCity.set(getReadableLocation(locationPoint.get()));

        return true;
    }

    private String getReadableLocation(GeoPoint geoPoint){

        if (geoPoint == null) {
            Log.e(TAG, "Location geopoint don't have any content");
            return "Not available";
        }

        Geocoder gcd = new Geocoder(FavorsMain.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (addresses.size() > 0) return addresses.get(0).getLocality();
        } catch (IOException e) {
            Log.e(TAG, "Failed to get geoPoint information");
        }
        return "Not available";
    }
}