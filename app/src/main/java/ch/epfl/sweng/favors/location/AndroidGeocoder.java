package ch.epfl.sweng.favors.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AndroidGeocoder extends GeocoderDispatcher {
    private static AndroidGeocoder androidGeocoder;
    private Geocoder geocoder;

    private AndroidGeocoder(Context context){
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public static AndroidGeocoder getGeocoder(Context context) {
        return new AndroidGeocoder(context);
    }

    @Override
    public List<Address> getFromLocationName(String locationName, int maxResults) throws IOException {
        return geocoder.getFromLocationName(locationName,maxResults);
    }

    @Override
    public List<Address> getFromLocation(double latitude, double longitude, int maxResults) throws IOException{
        return geocoder.getFromLocation(latitude, longitude, maxResults);
    }
}
