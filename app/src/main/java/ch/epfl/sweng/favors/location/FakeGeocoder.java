package ch.epfl.sweng.favors.location;

import android.location.Address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FakeGeocoder extends GeocoderDispatcher {
    public static final String FAKE_LOCATION_CITY = "Fake Lausanne";
    public static final double FAKE_LATITUDE = 46.5196535;
    public static final double FAKE_LONGITUDE = 6.6322734;

    private ArrayList<Address> addresses;

    private FakeGeocoder(){
        addresses = new ArrayList<>();
        addresses.add(new Address(Locale.getDefault()));
        addresses.get(0).setLocality(FAKE_LOCATION_CITY);
        addresses.get(0).setLatitude(FAKE_LATITUDE);
        addresses.get(0).setLongitude(FAKE_LONGITUDE);
    }

    public static FakeGeocoder getGeocoder() {
        return new FakeGeocoder();
    }

    @Override
    public List<Address> getFromLocationName(String locationName, int maxResults) throws IOException {
        return addresses;
    }

    @Override
    public List<Address> getFromLocation(double latitude, double longitude, int maxResults) throws IOException{
        return addresses;
    }
}
