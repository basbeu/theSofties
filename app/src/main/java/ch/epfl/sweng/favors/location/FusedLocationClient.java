package ch.epfl.sweng.favors.location;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import ch.epfl.sweng.favors.main.FavorsMain;

public class FusedLocationClient extends Location{

    private static String TAG = "FUSE_LOCATION";

    private FusedLocationProviderClient mFusedLocationClient;

    public static FusedLocationClient location = null;


    public FusedLocationClient(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(FavorsMain.getContext());
        requestPermission();
    }

    public static FusedLocationClient getInstance(){
        if(location == null){
            location = new FusedLocationClient();
        }

        return location;
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

    private LocationCallback locationCallback;

    public void removeLocationUpdates(){
        if(checkPermission() && locationCallback != null) mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void requestLocationUpdates(LocationRequest request, OnSuccessListener <android.location.Location> cb){
        if(checkPermission()) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        Log.e(TAG, "No location provided by GPS");
                        return;
                    }
                    for (android.location.Location l : locationResult.getLocations()) {
                        cb.onSuccess(l);
                    }
                }
            };
            mFusedLocationClient.requestLocationUpdates(request, locationCallback, null);
        }
    }

    public void getLastLocation(OnSuccessListener <android.location.Location> cb){
        if(checkPermission()) mFusedLocationClient.getLastLocation().addOnSuccessListener(cb);
    }

}
