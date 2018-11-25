package ch.epfl.sweng.favors.location;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class GeocodingLocation {

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    private static final String TAG = "GeocodingLocation";


    public static void getAddressFromLocation(final String locationAddress,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                GeocoderDispatcher geocoder = GeocoderDispatcher.getGeocoder(context);
                //Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                Double latitude = null;
                Double longitude = null;
                try {
                    List<Address> addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        latitude = address.getLatitude();
                        longitude = address.getLongitude();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (latitude != null && longitude != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putDouble(KEY_LATITUDE, latitude);
                        bundle.putDouble(KEY_LONGITUDE, longitude);
                        message.setData(bundle);
                    } else {
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}