package ch.epfl.sweng.favors.location;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.favors.FavorsMap;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class GeocodingLocationTest {

    @Rule
    public FragmentTestRule<FavorsMap> mFragmentTestRule = new FragmentTestRule<>(FavorsMap.class);

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
    }

    // test that check that getAddressFromLocation() sends the right messages on success of the geocoder
    @Test
    public void getAddressFromLocationSuccessMessageTest(){
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.mapView)).check(matches(isDisplayed()));
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        GeocodingLocation locationAddress = new GeocodingLocation();

        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 1){
                    Bundle bundle = msg.getData();
                    assertEquals(FakeGeocoder.FAKE_LATITUDE, bundle.getDouble("latitude"));
                    assertEquals(FakeGeocoder.FAKE_LONGITUDE, bundle.getDouble("longitude"));
                    Looper.myLooper().quitSafely();
                }
                return false;
            }
        };

        Handler handler = new Handler(callback);
        locationAddress.getAddressFromLocation(FakeGeocoder.FAKE_LOCATION_CITY, mFragmentTestRule.getFragment().getContext(), handler);

        Looper.loop();
    }

    // test that check that getAddressFromLocation() sends the right messages on failure of the geocoder
    @Test
    public void getAddressFromLocationFailureMessageTest(){
        ExecutionMode.getInstance().setGeocoderExecutionTestMode(ExecutionMode.GeocoderExecutionTestMode.FAILURE);
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.mapView)).check(matches(isDisplayed()));
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        GeocodingLocation locationAddress = new GeocodingLocation();

        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 2){
                    Bundle bundle = msg.getData();
                    assertEquals(GeocodingLocation.MES_FAILURE, bundle.getString(GeocodingLocation.KEY_FAILURE));

                    Looper.myLooper().quitSafely();
                }
                return false;
            }
        };

        Handler handler = new Handler(callback);
        locationAddress.getAddressFromLocation(FakeGeocoder.FAKE_LOCATION_CITY, mFragmentTestRule.getFragment().getContext(), handler);

        Looper.loop();

        ExecutionMode.getInstance().setGeocoderExecutionTestMode(ExecutionMode.GeocoderExecutionTestMode.SUCCESS);
    }

    @Test
    public void getAddressFromLocationExceptionTest(){
        ExecutionMode.getInstance().setGeocoderExecutionTestMode(ExecutionMode.GeocoderExecutionTestMode.EXCEPTION);
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.mapView)).check(matches(isDisplayed()));
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        GeocodingLocation locationAddress = new GeocodingLocation();

        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 3){
                    Bundle bundle = msg.getData();
                    assertEquals(GeocodingLocation.MES_EXCEPTION, bundle.getString(GeocodingLocation.KEY_EXCEPTION));

                    Looper.myLooper().quitSafely();
                }
                return false;
            }
        };

        Handler handler = new Handler(callback);
        locationAddress.getAddressFromLocation(FakeGeocoder.FAKE_LOCATION_CITY, mFragmentTestRule.getFragment().getContext(), handler);

        Looper.loop();

        ExecutionMode.getInstance().setGeocoderExecutionTestMode(ExecutionMode.GeocoderExecutionTestMode.SUCCESS);

    }
}
