package ch.epfl.sweng.favors.location;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class FakeLocationClient extends Location{

    public static FakeLocationClient getInstance(){
        if(location == null){
            location = new FakeLocationClient();
        }

        return location;
    }

    public static FakeLocationClient location = null;


    public FakeLocationClient(){}

    public void removeLocationUpdates(){

    }

    public void requestLocationUpdates(LocationRequest request, OnSuccessListener <android.location.Location> cb){
        getLastLocation(cb);
    }

    public void getLastLocation(OnSuccessListener <android.location.Location> cb){
        android.location.Location fakeLocation = new android.location.Location("fake");
        fakeLocation.setLatitude(46.5333d); //Fake latitude -> Lausanne
        fakeLocation.setLongitude(6.6667d); //Fake longitude -> Lausanne

        cb.onSuccess(fakeLocation);
    }

}

