package ch.epfl.sweng.favors.location;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Class that provide tools for finding location
 */
public final class GeocodingLocation {

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_FAILURE = "FAILURE";
    public static final String MES_FAILURE = "Fail to get latitude and longitude";
    public static final String KEY_EXCEPTION = "EXCEPTION";
    public static final String MES_EXCEPTION = "IOException : Unable to connect to Geocoder";

    private static final String TAG = "GeocodingLocation";

    /**
     * Method that search a location from a string and send an Address to a Handler
     * @param locationAddress String corresponding to an address to search
     * @param context Context from where the call come from
     * @param handler Handler to which the answers will be sent
     */
    public static void getAddressFromLocation(final String locationAddress,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                GeocoderDispatcher geocoder = GeocoderDispatcher.getGeocoder(context);
                String result = null;
                Double latitude = null;
                Double longitude = null;
                String city = null;
                String country = null;
                boolean ioException = false;

                try {
                    List<Address> addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        latitude = address.getLatitude();
                        longitude = address.getLongitude();
                        city = address.getLocality();
                        country = address.getCountryCode();

                    }
                } catch (IOException e) {
                    Log.e(TAG, MES_EXCEPTION, e);
                    ioException = true;
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (latitude != null && longitude != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putDouble("latitude", latitude);
                        bundle.putDouble("longitude", longitude);
                        bundle.putString("country", country);
                        bundle.putString("city", city);
                        message.setData(bundle);
                    } else if(!ioException){
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_FAILURE, MES_FAILURE);
                        message.setData(bundle);
                    }else{
                        message.what = 3;
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_EXCEPTION, MES_EXCEPTION);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}