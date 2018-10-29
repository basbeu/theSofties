package ch.epfl.sweng.favors.location;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.main.FavorsMain;
import ch.epfl.sweng.favors.utils.ExecutionMode;

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

    protected ch.epfl.sweng.favors.location.Location location = ch.epfl.sweng.favors.location.Location.getInstance();

    private boolean recurrent = false;

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


    private LocationRequest locationRequest(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 60 seconds
        locationRequest.setFastestInterval(5 * 1000); // 30 seconds
        return locationRequest;
    }


    public LocationHandler(boolean recurrent){
        isRecurrent(recurrent);
    }

    OnSuccessListener<Location> successListerner = new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            updateLocationInformations(location);
        }
    };

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
        User.setLocation(locationPoint.get());
        Log.d("location", "code:1000 - successfully obtained location of user - recurrent: " + recurrent);
        return true;
    }

    private String getReadableLocation(GeoPoint geoPoint){

        if (geoPoint == null) {
            Log.e(TAG, "Location geopoint does not have any content");
            return "Not available";
        }
        if(!ExecutionMode.getInstance().isTest()) {
            Geocoder gcd = new Geocoder(FavorsMain.getContext(), Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
                if (addresses.size() > 0) return addresses.get(0).getLocality();
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