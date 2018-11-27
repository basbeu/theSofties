package ch.epfl.sweng.favors.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Provide Geocoder from the android API (android.location.Geocoder)
 */
public final class AndroidGeocoder extends GeocoderDispatcher {
    private Geocoder geocoder;

    private AndroidGeocoder(Context context){
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    /**
     * @param context Context from where the geocoder will be called
     * @return a new Geocoder
     */
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
