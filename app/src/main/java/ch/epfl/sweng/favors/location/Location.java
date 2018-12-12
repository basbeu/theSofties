package ch.epfl.sweng.favors.location;

import android.databinding.ObservableArrayList;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;

import ch.epfl.sweng.favors.utils.ExecutionMode;

public abstract class Location {

    private static Location location = null;


    abstract public void removeLocationUpdates();

    abstract public void requestLocationUpdates(LocationRequest request, OnSuccessListener <android.location.Location> cb);

    abstract public void getLastLocation(OnSuccessListener <android.location.Location> cb);
    
    /**
     * @return Database that is the DB for the current session
     */
    public static Location getInstance(){
        if(location == null){
            if(ExecutionMode.getInstance().isTest()){
                location = FakeLocationClient.getInstance();
            }
            else{
                location = FusedLocationClient.getInstance();
            }
        }
        return location;
    }

}
