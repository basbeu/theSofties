package ch.epfl.sweng.favors.location;

import android.location.Geocoder;
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
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class GeocoderDispatcherTest {
    @Rule
    public FragmentTestRule<FavorsMap> mFragmentTestRule = new FragmentTestRule<>(FavorsMap.class);

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
    }

    @Test
    public void correctFakeGeocoderDuringTest(){
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.mapView)).check(matches(isDisplayed()));
        GeocoderDispatcher geocoder = GeocoderDispatcher.getGeocoder(mFragmentTestRule.getFragment().getContext());
        assertTrue(geocoder instanceof FakeGeocoder);
    }

    @Test
    public void correctAndroidGeocoderTest(){
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.mapView)).check(matches(isDisplayed()));
        ExecutionMode.getInstance().setTest(false);
        GeocoderDispatcher geocoder = GeocoderDispatcher.getGeocoder(mFragmentTestRule.getFragment().getContext());
        assertTrue(geocoder instanceof AndroidGeocoder);
        ExecutionMode.getInstance().setTest(true);
    }

}
