package ch.epfl.sweng.favors.location;

import android.location.Address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.favors.utils.ExecutionMode;

public class FakeGeocoder extends GeocoderDispatcher {
    public static final String FAKE_LOCATION_CITY = "Fake Lausanne";
    public static final double FAKE_LATITUDE = 46.5196535;
    public static final double FAKE_LONGITUDE = 6.6322734;

    private ArrayList<Address> addressesSuccess;
    private ArrayList<Address> addressesFailure;

    private FakeGeocoder(){
        addressesSuccess = new ArrayList<>();
        addressesSuccess.add(new Address(Locale.getDefault()));
        addressesSuccess.get(0).setLocality(FAKE_LOCATION_CITY);
        addressesSuccess.get(0).setLatitude(FAKE_LATITUDE);
        addressesSuccess.get(0).setLongitude(FAKE_LONGITUDE);

        addressesFailure = new ArrayList<>();
    }

    public static FakeGeocoder getGeocoder() {
        return new FakeGeocoder();
    }

    @Override
    public List<Address> getFromLocationName(String locationName, int maxResults) throws IOException {
        return fakeImplementation();
    }

    @Override
    public List<Address> getFromLocation(double latitude, double longitude, int maxResults) throws IOException{
        return fakeImplementation();
    }

    private List<Address> fakeImplementation() throws IOException {
        ArrayList<Address> addresses = new ArrayList<>();

        switch (ExecutionMode.getInstance().getGeocoderExecutionTestMode()){
            case SUCCESS:
                addresses = addressesSuccess;
                break;
            case FAILURE:
                addresses =  addressesFailure;
                break;
            case EXCEPTION:
                throw new IOException();
        }
        return addresses;
    }
}
