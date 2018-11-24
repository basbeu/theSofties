package ch.epfl.sweng.favors.location;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import ch.epfl.sweng.favors.utils.ExecutionMode;

public abstract class GeocoderDispatcher {

    public static GeocoderDispatcher getGeocoder(Context context) {
        if(ExecutionMode.getInstance().isTest()) {
            return FakeGeocoder.getGeocoder();
        }
        else{
            return AndroidGeocoder.getGeocoder(context);
        }
    }

    public abstract List<Address> getFromLocationName(String locationName, int maxResults) throws IOException;
    public abstract List<Address> getFromLocation(double latitude, double longitude, int maxResults) throws IOException;
}
